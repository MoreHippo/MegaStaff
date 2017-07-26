package com.morehippo.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.morehippo.megastaff.Manager;

public class ClearChatCommand implements CommandExecutor {
	private Manager manager;

	public ClearChatCommand(Manager manager) {
		this.manager = manager;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if (cmd.getName().equalsIgnoreCase("clearchat")) {
			if (sender.hasPermission(manager.getConfig().getString("basicStaffPermission"))
					|| sender.hasPermission(manager.getConfig().getString("adminStaffPermission"))
					|| !(sender instanceof Player)) {

				for (Player players : Bukkit.getOnlinePlayers()) {
					if (players.hasPermission(manager.getConfig().getString("basicStaffPermission"))
							|| players.hasPermission(manager.getConfig().getString("adminStaffPermission"))) {

						players.sendMessage(manager.getMessage("clearChat").replaceAll("%player%", sender.getName()));
					} else {
						for (int x = 0; x < 150; x++) {
							players.sendMessage(" ");
						}
						players.sendMessage(manager.getMessage("clearChat").replaceAll("%player%", sender.getName()));
					}
				}
			} else {
				sender.sendMessage(manager.getMessage("noPermission"));
				return true;
			}
		}
		return true;
	}

}
