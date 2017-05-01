package io.github.wolfleader116.hubblocks.bukkit.commands;

import java.util.Map;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import io.github.wolfleader116.hubblocks.bukkit.DataController;
import io.github.wolfleader116.hubblocks.bukkit.HubBlocks;
import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import io.github.wolfleader116.wolfapi.bukkit.data.Backend;
import io.github.wolfleader116.wolfapi.bukkit.data.Data;

public class MigrateSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("hubblocks.migrate")) {
			if (args.length == 0 ) {
				HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Please specify which backends to migrate to and from");
			} else if (args.length == 1) {
				HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Please specify which backend to migrate to");
			} else if (args.length >= 2) {
				String oldBackend = args[0];
				String newBackend = args[1];
				Map<String, Backend> backends = Data.getBackends();
				Set<String> keys = backends.keySet();
				Backend old = null;
				Backend b = null;
				boolean oldTrue = false;
				boolean newTrue = false;
				for (String s : keys) {
					if (s.equalsIgnoreCase(oldBackend)) {
						old = backends.get(s);
						oldTrue = true;
					}
					if (s.equalsIgnoreCase(newBackend)) {
						b = backends.get(s);
						newTrue = true;
					}
				}
				if (oldTrue && newTrue) {
					boolean success = true;
					success = DataController.migrateAnvils(old, b);
					if (success) {
						HubBlocks.message.sendPluginMessage(sender, "Data successfully migrated from " + old.getName() + " to " + b.getName() + "!");
					} else {
						HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "There was an issue while migrating data!");
					}
				} else if (!oldTrue && !newTrue) {
					HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Invalid backends: " + oldBackend + " and " + newBackend + "!");
				} else if (!newTrue) {
					HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Invalid backend: " + newBackend + "!");
				} else if (!oldTrue) {
					HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Invalid backend: " + oldBackend + "!");
				}
			}
			return true;
		} else {
			HubBlocks.message.sendPluginError(sender, Errors.NO_PERMISSION, "migrate plugin data!");
			return false;
		}
	}

}
