package com.morehippo.listeners;

import com.morehippo.megastaff.Manager;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

/**
 * Created by bradley on 28/07/2017.
 */
public class SilentChest implements Listener {
    private Manager manager;
    private ArrayList<String> inChest = new ArrayList<String>();

    public SilentChest(Manager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onChestInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getClickedBlock().getType() == Material.CHEST) {
            if (manager.checkVanishEnabled(player)) {
                Chest chest = (Chest) e.getClickedBlock();
                Inventory inv = chest.getInventory();

                e.setCancelled(true);
                player.sendMessage(manager.getMessage("silentChestOpen"));
                player.openInventory(inv);

                inChest.add(player.getName());

            }
        }

    }

    @EventHandler
    public void onChestClose(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        if(inv.getType() == InventoryType.CHEST){
            if(manager.checkVanishEnabled(player)){
                if(inChest.contains(player.getName())){
                    player.sendMessage(manager.getMessage("silentChestClose"));
                }
            }
        }

    }

}
