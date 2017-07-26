package com.morehippo.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.morehippo.megastaff.Manager;

public class VanishListener implements Listener {

	private Manager manager;

	public VanishListener(Manager manager) {
		this.manager = manager;
	}

	@EventHandler
	public void onLeftClick(EntityDamageByEntityEvent e) {

		if (!(e.getEntity() instanceof Player))
			return;
		if (!(e.getDamager() instanceof Player))
			return;
		Player player = (Player) e.getDamager();

		if (manager.checkVanishEnabled(player) == true) {
			e.setCancelled(true);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "You may not hit that while in vanish."));
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		if (manager.checkVanishEnabled(player) == true) {
			e.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You are in vanish.");
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		if (manager.checkVanishEnabled(player) == true) {
			e.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You are in vanish.");
		}
	}

	@EventHandler
	public void onRightClickPlayer(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof Player) {
			if (manager.checkVanishEnabled(e.getPlayer()) == true) {
				e.getPlayer().sendMessage(e.getRightClicked().getName() + "'s information.");
			}
		}
	}

	@EventHandler
	public void onPlayerJoinHideStaff(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		for (Player vanished : manager.getVanishedList()) {
			if (vanished.equals(player)) {
				for (Player players : Bukkit.getOnlinePlayers()) {
					if (players.hasPermission(manager.getConfig().getString("adminStaffPermission"))
							|| players.hasPermission(manager.getConfig().getString("basicStaffPermission"))) {
						players.sendMessage(manager.getMessage("staffLogin").replaceAll("%player%", player.getName()));
					}
					if (!player.hasPermission(manager.getConfig().getString("adminStaffPermission"))) {
						players.hidePlayer(vanished);
					}
				}
			} else {
				player.hidePlayer(vanished);
			}

		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();

		if (manager.checkVanishEnabled(player)) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				players.sendMessage(manager.getMessage("staffLeave").replaceAll("%player%", player.getName()));
			}
		}

	}

}
