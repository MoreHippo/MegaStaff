package com.morehippo.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import com.morehippo.megastaff.Manager;

import net.md_5.bungee.api.ChatColor;

public class FreezeListeners implements Listener {
	private Manager manager;

	public FreezeListeners(Manager manager) {
		this.manager = manager;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (e.isCancelled())
			return;

		Player player = e.getPlayer();

		if (manager.getFrozen().contains(player.getName())) {
			if (manager.getConfig().getBoolean("letPlayerLookAroundWhileFrozen")) {
				double px = e.getFrom().getX();
				double py = e.getFrom().getY();
				double pz = e.getFrom().getZ();

				if (px != e.getTo().getX() || py != e.getTo().getY() || pz != e.getTo().getZ()) {
					e.setTo(e.getFrom());
					player.sendMessage(manager.getMessage("onFrozenPlayerMove"));
				}

			} else {
				e.setCancelled(true);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Player player = (Player) e.getPlayer();
		if (manager.getConfig().getBoolean("displayGUIWhenFrozen")) {
			if (manager.getFrozen().contains(player.getName())) {
				Bukkit.getScheduler().scheduleAsyncDelayedTask(manager.plugin, new Runnable() {
					@Override
					public void run() {
						Inventory inv = e.getInventory();
						player.openInventory(inv);
					}
				}, 5L);
			}
		}
	}
	
	@EventHandler
	public void onInventoryInteract(InventoryInteractEvent e) {
		Player player = (Player) e.getWhoClicked();
		
		if (manager.getConfig().getBoolean("displayGUIWhenFrozen")) {
			if(manager.getFrozen().contains(player.getName())) {
				if (e.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', manager.getConfig().getString("guiName")))) {
					e.setCancelled(true);
				}
			}
		}
		
	}
	
	@EventHandler
	public void onLeaveWhileFrozen(PlayerQuitEvent e) {
		
		Player player = e.getPlayer();
		
		String banmsg1 = manager.getMessage("banMessageOnlogOff");
		
		String banmsg = banmsg1.replaceAll("%player", player.getName());
		
		if (manager.isFrozen(player)) {
			Bukkit.getPlayer(player.getName()).setBanned(true);
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', banmsg));
		}
		
	}

}
