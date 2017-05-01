package io.github.wolfleader116.hubblocks.bukkit.commands;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.wolfleader116.hubblocks.bukkit.DataController;
import io.github.wolfleader116.hubblocks.bukkit.HubBlocks;
import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import net.md_5.bungee.api.ChatColor;

public class AnvilSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("hubblocks.infiniteanvil")) {
				if (args.length == 0 ) {
					HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Either add or remove an infinite anvil!");
				} else {
					if (args[0].equalsIgnoreCase("add")) {
						Block b = ((Player) sender).getTargetBlock((HashSet<Material>) null, 5);
						if (b.getType() == Material.ANVIL) {
							boolean result = DataController.addAnvil(b.getLocation());
							if (result) {
								HubBlocks.message.sendPluginMessage(sender, "Anvil is now indestructible!");
							} else {
								HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Anvil was already indestructible or could not be made indestructible!");
							}
						} else {
							HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Block must be an anvil!");
							return false;
						}
					} else if (args[0].equalsIgnoreCase("remove")) {
						Block b = ((Player) sender).getTargetBlock((HashSet<Material>) null, 5);
						if (b.getType() == Material.ANVIL) {
							boolean result = DataController.removeAnvil(b.getLocation());
							if (result) {
								HubBlocks.message.sendPluginMessage(sender, "Anvil is now destructible!");
							} else {
								HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Anvil was already destructible or could not be made destructible!");
							}
						} else {
							HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Block must be an anvil!");
							return false;
						}
					} else {
						HubBlocks.message.sendPluginError(sender, Errors.CUSTOM, "Invalid subcommand! Use /hubblocks help for a list of valid subcommands!");
					}
				}
				return true;
			} else {
				HubBlocks.message.sendPluginError(sender, Errors.NO_PERMISSION, "create an infinite anvil!");
				return false;
			}
		} else {
			Message.sendConsoleMessage(ChatColor.RED + "Only players can use this subcommand!");
			return false;
		}
	}

}
