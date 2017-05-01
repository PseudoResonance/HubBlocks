package io.github.wolfleader116.hubblocks.bukkit;

import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import io.github.wolfleader116.wolfapi.bukkit.Config;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import io.github.wolfleader116.wolfapi.bukkit.data.Backend;
import io.github.wolfleader116.wolfapi.bukkit.data.Data;
import io.github.wolfleader116.wolfapi.bukkit.data.FileBackend;
import io.github.wolfleader116.wolfapi.bukkit.data.MysqlBackend;
import io.github.wolfleader116.wolfapi.bukkit.data.SqliteBackend;
import net.md_5.bungee.api.ChatColor;

public class DataController {
	
	private static List<Location> anvilLocations = new ArrayList<Location>();
	private static Backend backend;
	
	private static Config c;
	
	private static Connection con;
	private static Statement s;
	private static String prefix;
	
	private static boolean error = false;
	
	public static void updateBackend() {
		try {
			s.close();
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {}
		backend = Data.getBackend();
		if (backend instanceof FileBackend) {
			FileBackend fb = (FileBackend) backend;
			c = new Config(fb.getFolder(), fb.getFile(), HubBlocks.plugin);
		} else if (backend instanceof MysqlBackend) {
			MysqlBackend mb = (MysqlBackend) backend;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://" + mb.getHost() + ":" + mb.getPort() + "/" + mb.getDatabase(), mb.getUsername(), mb.getPassword());
				s = con.createStatement();
				prefix = mb.getPrefix();
				s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + prefix + "Anvils` (x int, y int, z int, world VARCHAR(100));");
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				error = true;
			}
		} else if (backend instanceof SqliteBackend) {
			SqliteBackend sb = (SqliteBackend) backend;
			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:" + sb.getLocation().toString() + ".db");
				s = con.createStatement();
				prefix = sb.getPrefix();
				s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + prefix + "Anvils` (x int, y int, z int, world VARCHAR(100));");
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				error = true;
			}
		}
		loadAnvils();
	}
	
	public static boolean migrateAnvils(Backend old, Backend b) {
		boolean success = true;
		ArrayList<Location> anvils = getAnvils(old);
		success = wipe(b);
		success = addAnvils(anvils, b);
		return success;
	}
	
	public static void loadAnvils() {
		anvilLocations = new ArrayList<Location>();
		if (backend instanceof FileBackend) {
			List<String> list = c.getConfig().getStringList("HubBlocks.Anvils");
			for (String s : list) {
				String[] parts = s.split(",");
				Location l = new Location(Bukkit.getWorld(parts[3]), Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
				anvilLocations.add(l);
			}
		} else if (backend instanceof MysqlBackend || backend instanceof SqliteBackend) {
			try {
				ResultSet set = s.executeQuery("SELECT * FROM `" + prefix + "Anvils`");
				while (set.next()) {
					int x = set.getInt("x");
					int y = set.getInt("y");
					int z = set.getInt("z");
					String world = set.getString("world");
					Location l = new Location(Bukkit.getWorld(world), x, y, z);
					anvilLocations.add(l);
				}
				set.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean addAnvil(Location l) {
		if (!anvilLocations.contains(l)) {
			anvilLocations.add(l);
			if (!error) {
				if (backend instanceof FileBackend) {
					List<String> list = c.getConfig().getStringList("HubBlocks.Anvils");
					list.add(l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + "," + l.getWorld().getName());
					c.set("HubBlocks.Anvils", list);
					c.save();
					return true;
				} else if (backend instanceof MysqlBackend || backend instanceof SqliteBackend) {
					try {
						s.executeUpdate("INSERT INTO `" + prefix + "Anvils` VALUES (" + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + ", '" + l.getWorld().getName() + "');");
						return true;
					} catch (SQLException e) {
						e.printStackTrace();
						return false;
					}
				}
			} else {
				Message.sendConsoleMessage(ChatColor.RED + "Backend configuration error! Cannot save data!");
			}
			return false;
		}
		return false;
	}
	
	public static boolean removeAnvil(Location l) {
		if (anvilLocations.contains(l)) {
			anvilLocations.remove(l);
			if (!error) {
				if (backend instanceof FileBackend) {
					List<String> list = c.getConfig().getStringList("HubBlocks.Anvils");
					list.remove(l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + "," + l.getWorld().getName());
					c.set("HubBlocks.Anvils", list);
					c.save();
					return true;
				} else if (backend instanceof MysqlBackend || backend instanceof SqliteBackend) {
					try {
						s.executeUpdate("DELETE FROM `" + prefix + "Anvils` WHERE x=" + l.getBlockX() + " AND y=" + l.getBlockY() + " AND z=" + l.getBlockZ() + " AND world='" + l.getWorld().getName() + "';");
						return true;
					} catch (SQLException e) {
						e.printStackTrace();
						return false;
					}
				}
			} else {
				Message.sendConsoleMessage(ChatColor.RED + "Backend configuration error! Cannot save data!");
			}
			return false;
		}
		return false;
	}
	
	public static boolean isAnvil(Location l) {
		return anvilLocations.contains(l);
	}
	
	public static ArrayList<Location> getAnvils(Backend b) {
		ArrayList<Location> anvilLocations = new ArrayList<Location>();
		if (b instanceof FileBackend) {
			FileBackend fb = (FileBackend) b;
			Config config = new Config(fb.getFolder(), fb.getFile(), HubBlocks.plugin);
			List<String> list = config.getConfig().getStringList("HubBlocks.Anvils");
			for (String s : list) {
				String[] parts = s.split(",");
				Location l = new Location(Bukkit.getWorld(parts[3]), Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
				anvilLocations.add(l);
			}
		} else if (b instanceof MysqlBackend) {
			MysqlBackend mb = (MysqlBackend) b;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://" + mb.getHost() + ":" + mb.getPort() + "/" + mb.getDatabase(), mb.getUsername(), mb.getPassword());
				Statement s = con.createStatement();
				ResultSet set = s.executeQuery("SELECT * FROM `" + mb.getPrefix() + "Anvils`");
				while (set.next()) {
					int x = set.getInt("x");
					int y = set.getInt("y");
					int z = set.getInt("z");
					String world = set.getString("world");
					Location l = new Location(Bukkit.getWorld(world), x, y, z);
					anvilLocations.add(l);
				}
				s.close();
				con.close();
				set.close();
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				error = true;
			}
		} else if (b instanceof SqliteBackend) {
			SqliteBackend sb = (SqliteBackend) b;
			try {
				Class.forName("org.sqlite.JDBC");
				Connection con = DriverManager.getConnection("jdbc:sqlite:" + sb.getLocation().toString() + ".db");
				Statement s = con.createStatement();
				ResultSet set = s.executeQuery("SELECT * FROM `" + sb.getPrefix() + "Anvils`");
				while (set.next()) {
					int x = set.getInt("x");
					int y = set.getInt("y");
					int z = set.getInt("z");
					String world = set.getString("world");
					Location l = new Location(Bukkit.getWorld(world), x, y, z);
					anvilLocations.add(l);
				}
				s.close();
				con.close();
				set.close();
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				error = true;
			}
		}
		return anvilLocations;
	}
	
	public static boolean addAnvils(ArrayList<Location> loc, Backend b) {
		boolean error = false;
		if (b instanceof FileBackend) {
			FileBackend fb = (FileBackend) b;
			Config config = new Config(fb.getFolder(), fb.getFile(), HubBlocks.plugin);
			List<String> list = config.getConfig().getStringList("HubBlocks.Anvils");
			for (Location l : loc) {
				list.add(l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + "," + l.getWorld().getName());
			}
			config.set("HubBlocks.Anvils", list);
			config.save();
		} else if (b instanceof MysqlBackend) {
			MysqlBackend mb = (MysqlBackend) b;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://" + mb.getHost() + ":" + mb.getPort() + "/" + mb.getDatabase(), mb.getUsername(), mb.getPassword());
				Statement s = con.createStatement();
				s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + mb.getPrefix() + "Anvils` (x int, y int, z int, world VARCHAR(100));");
				for (Location l : loc) {
					s.executeUpdate("INSERT INTO `" + mb.getPrefix() + "Anvils` VALUES (" + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + ", '" + l.getWorld().getName() + "');");
				}
				s.close();
				con.close();
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				error = true;
			}
		} else if (b instanceof SqliteBackend) {
			SqliteBackend sb = (SqliteBackend) b;
			try {
				Class.forName("org.sqlite.JDBC");
				Connection con = DriverManager.getConnection("jdbc:sqlite:" + sb.getLocation().toString() + ".db");
				Statement s = con.createStatement();
				s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + sb.getPrefix() + "Anvils` (x int, y int, z int, world VARCHAR(100));");
				for (Location l : loc) {
					s.executeUpdate("INSERT INTO `" + sb.getPrefix() + "Anvils` VALUES (" + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + ", '" + l.getWorld().getName() + "');");
				}
				s.close();
				con.close();
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				error = true;
			}
		}
		if (error) {
			Message.sendConsoleMessage(ChatColor.RED + "Backend configuration error! Cannot complete request!");
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean wipe(Backend b) {
		boolean error = false;
		if (b instanceof FileBackend) {
			FileBackend fb = (FileBackend) b;
			Config config = new Config(fb.getFolder(), fb.getFile(), HubBlocks.plugin);
			config.set("HubBlocks.Anvils", null);
			config.save();
		} else if (b instanceof MysqlBackend) {
			MysqlBackend mb = (MysqlBackend) b;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://" + mb.getHost() + ":" + mb.getPort() + "/" + mb.getDatabase(), mb.getUsername(), mb.getPassword());
				Statement s = con.createStatement();
				s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + mb.getPrefix() + "Anvils` (x int, y int, z int, world VARCHAR(100));");
				s.executeUpdate("TRUNCATE `" + mb.getPrefix() + "Anvils`;");
				s.close();
				con.close();
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				error = true;
			}
		} else if (b instanceof SqliteBackend) {
			SqliteBackend sb = (SqliteBackend) b;
			try {
				Class.forName("org.sqlite.JDBC");
				Connection con = DriverManager.getConnection("jdbc:sqlite:" + sb.getLocation().toString() + ".db");
				Statement s = con.createStatement();
				s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + sb.getPrefix() + "Anvils` (x int, y int, z int, world VARCHAR(100));");
				s.executeUpdate("TRUNCATE `" + sb.getPrefix() + "Anvils`;");
				s.close();
				con.close();
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				error = true;
			}
		}
		if (error) {
			Message.sendConsoleMessage(ChatColor.RED + "Backend configuration error! Cannot complete request!");
			return false;
		} else {
			return true;
		}
	}

}
