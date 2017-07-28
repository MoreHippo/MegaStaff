package com.morehippo.megastaff;

import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;

public class Manager implements Listener {
	public MegaStaff plugin;

	public Manager(MegaStaff plugin) {
		this.plugin = plugin;
	}

	public String getMessage(String identifier) {
		if (plugin.messages.getString(identifier) != null) {
			return ChatColor.translateAlternateColorCodes('&', getPrefix() + plugin.messages.getString(identifier));
		}
		return null;
	}

	public FileConfiguration getConfig() {
		return plugin.config;
	}

	public String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', plugin.messages.getString("prefix"));
	}

	public String getStaffChatPrefix() {
		return ChatColor.translateAlternateColorCodes('&', plugin.messages.getString("staffChatPrefix"));
	}
	
	public ArrayList<String> getFrozen() {
		return plugin.frozen;
	}

	public ArrayList<String> getInStaffChatlist() {
		return plugin.staffchat;
	}

	public ArrayList<Player> getVanishedList() {
		return plugin.vanish;
	}

	public boolean checkVanishEnabled(Player p) {
		return plugin.vanish.contains(p);
	}

	public boolean isFrozen(Player p) {
		return plugin.frozen.contains(p);
	}

	public void setVanishEnabled(Player player, boolean b) {
		if (b) {
			if (!(plugin.vanish.contains(player))) {
				plugin.vanish.add(player);
				player.sendMessage(getMessage("vanishEnabled"));
				for (Player online : Bukkit.getOnlinePlayers()) {
					if (!(online.hasPermission(getConfig().getString("basicStaffPermission")))) {
						online.hidePlayer(player);
						
					}

				}

			}

		}
		if (!b) {
			if (plugin.vanish.contains(player)) {
				plugin.vanish.remove(player);
				player.sendMessage(getMessage("vanishDisabled"));
				for (Player online : Bukkit.getOnlinePlayers()) {
					online.showPlayer(player);

				}

			}
		}

	}

	public boolean checkPluginEnabled(String name){
		final PluginManager pluginManager = plugin.getServer().getPluginManager();
		final Plugin checkPlugin = pluginManager.getPlugin(name);

		if (checkPlugin != null && checkPlugin.isEnabled()){
			return true;
		}
		return false;
	}

	public String getPrefix(final Player base) {
		final AnjoPermissionsHandler handler = plugin.groupManager.getWorldsHolder().getWorldPermissions(base);
		if (handler == null) {
			return null;
		}
		return handler.getUserPrefix(base.getName());
	}

	public void sendStaffChatMessage(Player player, String message){
		for(Player players : Bukkit.getOnlinePlayers()){
            if (players.hasPermission(getConfig().getString("basicStaffPermission"))
                    || players.hasPermission(getConfig().getString("adminStaffPermission"))) {
                if (checkPluginEnabled("PermissionsEx") && !checkPluginEnabled("GroupManager")) {
                    PermissionUser user = PermissionsEx.getUser(player);

                    String prefix = ChatColor.translateAlternateColorCodes('&', user.getPrefix());
                    message = ChatColor.translateAlternateColorCodes('&', message);
                    message = ChatColor.translateAlternateColorCodes('&', plugin.messages.getString("staffChatFormat")
                            .replaceAll("%prefix%", prefix)
                            .replaceAll("%player%", player.getName())
                            .replaceAll("%message%", message));

                    players.sendMessage(message);
                }
            }
		}
	}

	public void setFrozen(Player player, Player sender) {

		if (plugin.frozen.contains(player.getName())) {
			plugin.frozen.remove(player.getName());
			player.sendMessage(getMessage("beenUnfrozen").replaceAll("%player%", sender.getName()));
			sender.sendMessage(getMessage("unfrozenPlayer").replaceAll("%target%", player.getName()));

			player.closeInventory();

			return;
		} else if (!plugin.frozen.contains(player.getName())) {
			plugin.frozen.add(player.getName());
			player.sendMessage(getMessage("beenFrozen").replaceAll("%player%", sender.getName()));
			sender.sendMessage(getMessage("frozenPlayer").replaceAll("%target%", player.getName()));

			if (getConfig().getBoolean("displayGUIWhenFrozen")) {
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("You have been frozen!");
				lore.add("If you log off, you will be banned!");
				ItemStack l = new ItemStack(Material.DIAMOND);
				ItemMeta il = l.getItemMeta();
				il.setDisplayName(ChatColor.RED + "YOU HAVE BEEN FROZEN");
				il.setLore((lore));
				Inventory inv = Bukkit.createInventory(null, 9,
						ChatColor.translateAlternateColorCodes('&', getConfig().getString("guiName")));

				inv.setItem(5, l);
				player.openInventory(inv);
			}
			return;

		}

	}

	public void noPermission(CommandSender sender) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage("noPermission")));
	}
	
	public void notAPlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage("mustBeAPlayer")));
	}
	
	
	public void openChestVanished(Block block, Player player) {
		Chest chest = (Chest) block.getState();
		Inventory inv = Bukkit.getServer().createInventory(player, chest.getInventory().getSize());
		if (chest.getInventory().getContents() != null) {
			inv.setContents(chest.getInventory().getContents());
		}
		chest.update();
		player.openInventory(inv);
		player.sendMessage(getMessage("silentChestOpen"));
	}

}
