package com.morehippo.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.morehippo.megastaff.Manager;

public class FreezeCommand implements CommandExecutor {
	private Manager manager;

	public FreezeCommand(Manager manager) {
		this.manager = manager;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("freeze")) {
				if (player.hasPermission(manager.getConfig().getString("basicStaffPermission"))) {

					if (args.length == 0) {
						player.sendMessage(manager.getMessage("specifyPlayer"));
						return true;
					} else if (args.length == 1) {
						Player target = Bukkit.getPlayer(args[0]);
						if (target == null || !player.isOnline()) {
							player.sendMessage(manager.getMessage("playerNotFound"));
							return true;
						}

						manager.setFrozen(target, player);
						return true;
					}

				} else {
					player.sendMessage(manager.getMessage("noPermission"));
					return true;
				}
			}
		}
		return true;
	}

}
