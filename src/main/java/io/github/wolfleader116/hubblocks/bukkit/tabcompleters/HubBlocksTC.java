package io.github.wolfleader116.hubblocks.bukkit.tabcompleters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import io.github.wolfleader116.wolfapi.bukkit.data.Backend;
import io.github.wolfleader116.wolfapi.bukkit.data.Data;

public class HubBlocksTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (args.length == 1) {
			possible.add("help");
			if (sender.hasPermission("hubblocks.reload")) {
				possible.add("reload");
			}
			if (sender.hasPermission("hubblocks.reset")) {
				possible.add("reset");
			}
			if (sender.hasPermission("hubblocks.infiniteanvil")) {
				possible.add("anvil");
			}
			if (sender.hasPermission("hubblocks.migrate")) {
				possible.add("migrate");
			}
			if (args[0].equalsIgnoreCase("")) {
				return possible;
			} else {
				List<String> checked = new ArrayList<String>();
				for (String check : possible) {
					if (check.toLowerCase().startsWith(args[0].toLowerCase())) {
						checked.add(check);
					}
				}
				return checked;
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("anvil") && sender.hasPermission("hubblocks.infiniteanvil")) {
				possible.add("add");
				possible.add("remove");
				if (args[1].equalsIgnoreCase("")) {
					return possible;
				} else {
					List<String> checked = new ArrayList<String>();
					for (String check : possible) {
						if (check.toLowerCase().startsWith(args[1].toLowerCase())) {
							checked.add(check);
						}
					}
					return checked;
				}
			} else if (args[0].equalsIgnoreCase("migrate") && sender.hasPermission("hubblocks.migrate")) {
				Map<String, Backend> backends = Data.getBackends();
				possible.addAll(backends.keySet());
				if (args[1].equalsIgnoreCase("")) {
					return possible;
				} else {
					List<String> checked = new ArrayList<String>();
					for (String check : possible) {
						if (check.toLowerCase().startsWith(args[1].toLowerCase())) {
							checked.add(check);
						}
					}
					return checked;
				}
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("migrate") && sender.hasPermission("hubblocks.migrate")) {
				Map<String, Backend> backends = Data.getBackends();
				possible.addAll(backends.keySet());
				if (args[2].equalsIgnoreCase("")) {
					return possible;
				} else {
					List<String> checked = new ArrayList<String>();
					for (String check : possible) {
						if (check.toLowerCase().startsWith(args[2].toLowerCase())) {
							checked.add(check);
						}
					}
					return checked;
				}
			}
		}
		return null;
	}

}
