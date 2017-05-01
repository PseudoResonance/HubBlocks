package io.github.wolfleader116.hubblocks.bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.wolfleader116.wolfapi.bukkit.ConfigOption;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import net.md_5.bungee.api.ChatColor;

public class ConfigOptions implements ConfigOption {
	
	public static List<String> regions = new ArrayList<String>();
	
	public static boolean updateConfig() {
		if (HubBlocks.plugin.getConfig().getInt("Version") == 1) {
			Message.sendConsoleMessage(ChatColor.GREEN + "Config is up to date!");
		} else {
			try {
				String oldFile = "";
				File conf = new File(HubBlocks.plugin.getDataFolder(), "config.yml");
				if (new File(HubBlocks.plugin.getDataFolder(), "config.yml.old").exists()) {
					for (int i = 1; i > 0; i++) {
						if (!(new File(HubBlocks.plugin.getDataFolder(), "config.yml.old" + i).exists())) {
							conf.renameTo(new File(HubBlocks.plugin.getDataFolder(), "config.yml.old" + i));
							oldFile = "config.yml.old" + i;
							break;
						}
					}
				} else {
					conf.renameTo(new File(HubBlocks.plugin.getDataFolder(), "config.yml.old"));
					oldFile = "config.yml.old";
				}
				HubBlocks.plugin.saveDefaultConfig();
				HubBlocks.plugin.reloadConfig();
				Message.sendConsoleMessage(ChatColor.GREEN + "Config is up to date! Old config file renamed to " + oldFile + ".");
			} catch (Exception e) {
				Message.sendConsoleMessage(ChatColor.RED + "Error while updating config!");
				return false;
			}
		}
		return true;
	}
	
	public void reloadConfig() {
		regions = HubBlocks.plugin.getConfig().getStringList("Regions");
		DataController.updateBackend();
	}

}