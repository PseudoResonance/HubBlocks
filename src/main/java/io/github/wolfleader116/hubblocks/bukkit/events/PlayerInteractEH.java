package io.github.wolfleader116.hubblocks.bukkit.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.wolfleader116.hubblocks.bukkit.DataController;

public class PlayerInteractEH implements Listener {

	@EventHandler
	public void playerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			if (b.getType() == Material.ANVIL) {
				Location l = b.getLocation();
				if (DataController.isAnvil(l)) {
					e.getPlayer().openInventory(Bukkit.createInventory(null, InventoryType.ANVIL));
					e.setCancelled(true);
				}
			}
		}
	}

}
