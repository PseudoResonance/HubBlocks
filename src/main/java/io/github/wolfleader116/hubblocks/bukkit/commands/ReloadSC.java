package io.github.wolfleader116.hubblocks.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.wolfleader116.hubblocks.bukkit.HubBlocks;
import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;

public class ReloadSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("hubblocks.reload")) {
				try {
					HubBlocks.plugin.reloadConfig();
				} catch (Exception e) {
					HubBlocks.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				HubBlocks.getConfigOptions().reloadConfig();
				HubBlocks.message.sendPluginMessage(sender, "Plugin config reloaded!");
				return true;
			} else {
				HubBlocks.message.sendPluginError(sender, Errors.NO_PERMISSION, "reload the config!");
				return false;
			}
		} else {
			try {
				HubBlocks.plugin.reloadConfig();
			} catch (Exception e) {
				HubBlocks.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			HubBlocks.getConfigOptions().reloadConfig();
			HubBlocks.message.sendPluginMessage(sender, "Plugin config reloaded!");
			return true;
		}
	}

}
