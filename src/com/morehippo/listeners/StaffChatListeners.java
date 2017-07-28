package com.morehippo.listeners;

import com.morehippo.megastaff.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StaffChatListeners implements Listener {
    private Manager manager;
	
	public StaffChatListeners(Manager manager) {
		this.manager = manager;
	}
	
	@EventHandler
	public void onPlayerChatWhileInStaffChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

        if (manager.getInStaffChatlist().contains(player.getName())) {
            e.setCancelled(true);
            manager.sendStaffChatMessage(player, message);
            return;
        }
    }

}
