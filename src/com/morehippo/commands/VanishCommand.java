package com.morehippo.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.morehippo.megastaff.Manager;

public class VanishCommand implements CommandExecutor {

	private Manager manager;

	public VanishCommand(Manager manager) {
		this.manager = manager;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if (cmd.getName().equalsIgnoreCase("vanish")) {
			
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', manager.getMessage("mustBeAPlayer")));
				return true;
			}
			
			if (sender.hasPermission(manager.getConfig().getString("basicStaffPermission"))
					|| sender.hasPermission(manager.getConfig().getString("adminStaffPermission"))) {

				Player player = (Player) sender;
				
				if (manager.checkVanishEnabled(player)) {
					manager.setVanishEnabled(player, false);
					return true;
				} else if(!manager.checkVanishEnabled(player)) {
					manager.setVanishEnabled(player, true);
					return true;
				}
				
			} else {
				sender.sendMessage(manager.getMessage("noPermission"));
				return true;
			}
		}
		return true;
	}
	
}
