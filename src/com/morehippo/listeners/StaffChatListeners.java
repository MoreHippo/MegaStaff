package com.morehippo.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.morehippo.megastaff.Manager;

public class StaffChatListeners implements Listener {
	
	Manager manager;
	
	public StaffChatListeners(Manager manager) {
		this.manager = manager;
	}
	
	@EventHandler
	public void onPlayerChatWhileInStaffChat(AsyncPlayerChatEvent e) {
		 Player player = e.getPlayer();
		
		 if (manager.getInStaffChatlist().contains(player.getName())) {
			 e.setCancelled(true);
			 
			 for (Player online : Bukkit.getOnlinePlayers()) {
				 if (online.hasPermission(manager.getConfig().getString("basicStaffPermission"))
							|| online.hasPermission(manager.getConfig().getString("adminStaffPermission"))) {
					 
					 String m1 = manager.getMessage("staffChatFormat");
					 String m2 = m1.replaceAll("%player%", player.getName());
					 String message = m2.replaceAll("%message%", e.getMessage());
					 
					 online.sendMessage(ChatColor.translateAlternateColorCodes('&', manager.getMessage("staffChatPrefix") + message));
					 
				 } else {
					 return;
				 }
			 }
			 
		 } else {
			 return;
		 }
		
	}

}
