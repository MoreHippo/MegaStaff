package com.morehippo.commands;

import com.morehippo.megastaff.Manager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {
	private Manager manager;
	
	public StaffChatCommand(Manager manager) {
		this.manager = manager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("staffchat")) {
			if (sender instanceof Player){
                Player player = (Player) sender;
				if (sender.hasPermission(manager.getConfig().getString("basicStaffPermission"))
						|| sender.hasPermission(manager.getConfig().getString("adminStaffPermission"))) {
					
					if(manager.getInStaffChatlist().contains(player.getName())) {
						manager.getInStaffChatlist().remove(player.getName());
						player.sendMessage(manager.getMessage("staffChatDisabled"));
						return true;
					} 
					
					if(!manager.getInStaffChatlist().contains(player.getName())) {
						manager.getInStaffChatlist().add(player.getName());
						
						player.sendMessage(manager.getMessage("staffChatEnabled"));
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
