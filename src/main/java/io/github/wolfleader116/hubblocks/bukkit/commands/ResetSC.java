package io.github.wolfleader116.hubblocks.bukkit.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.wolfleader116.hubblocks.bukkit.HubBlocks;
import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;

public class ResetSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("hubblocks.reset")) {
				try {
					File conf = new File(HubBlocks.plugin.getDataFolder(), "config.yml");
					conf.delete();
					HubBlocks.plugin.saveDefaultConfig();
					HubBlocks.plugin.reloadConfig();
				} catch (Exception e) {
					HubBlocks.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				HubBlocks.getConfigOptions().reloadConfig();
				HubBlocks.message.sendPluginMessage(sender, "Plugin config reset!");
				return true;
			} else {
				HubBlocks.message.sendPluginError(sender, Errors.NO_PERMISSION, "reset the config!");
				return false;
			}
		} else {
			try {
				File conf = new File(HubBlocks.plugin.getDataFolder(), "config.yml");
				conf.delete();
				HubBlocks.plugin.saveDefaultConfig();
				HubBlocks.plugin.reloadConfig();
			} catch (Exception e) {
				HubBlocks.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			HubBlocks.getConfigOptions().reloadConfig();
			HubBlocks.message.sendPluginMessage(sender, "Plugin config reset!");
			return true;
		}
	}

}
