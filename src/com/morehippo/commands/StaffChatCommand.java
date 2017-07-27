package com.morehippo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.morehippo.megastaff.Manager;

import net.md_5.bungee.api.ChatColor;

public class StaffChatCommand implements CommandExecutor {

	Manager manager;
	
	public StaffChatCommand(Manager manager) {
		this.manager = manager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("staffchat")) {
			if (sender instanceof Player){
				if (sender.hasPermission(manager.getConfig().getString("basicStaffPermission"))
						|| sender.hasPermission(manager.getConfig().getString("adminStaffPermission"))) {
					Player player = (Player) sender;
					
					if(manager.getInStaffChatlist().contains(player.getName())) {
						manager.getInStaffChatlist().remove(player.getName());
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', manager.getMessage("staffChatDisabled")));
						return true;
					} 
					
					if(!manager.getInStaffChatlist().contains(player.getName())) {
						manager.getInStaffChatlist().add(player.getName());
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', manager.getMessage("staffChatEnabled")));
						return true;
					}
					
					
				} else {
					manager.noPermission(sender);
				}
				
			} else {
				manager.notAPlayer(sender);
			}
		}
		
		
		
		
		return true;
	}

}
