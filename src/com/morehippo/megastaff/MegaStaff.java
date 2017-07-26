package com.morehippo.megastaff;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.morehippo.commands.ClearChatCommand;
import com.morehippo.commands.FreezeCommand;
import com.morehippo.commands.VanishCommand;
import com.morehippo.listeners.FreezeListeners;
import com.morehippo.listeners.VanishListener;

public class MegaStaff extends JavaPlugin implements Listener {
	public File cfile;
	public FileConfiguration config;
	public File messageFile = null;
	public FileConfiguration messages = null;	
	
	public ArrayList<Player> vanish = new ArrayList<Player>();
	public ArrayList<String> staffchat = new ArrayList<String>();
	public ArrayList<String> frozen = new ArrayList<String>();
	
	@Override
	public void onEnable(){
		
		cfile = new File(getDataFolder(), "config.yml");
		if (!cfile.exists()) {
			saveDefaultConfig();
		}
		
		messageFile = new File(getDataFolder(), "messages.yml");
		if (!messageFile.exists()){
			saveDefaultMessages();
		}
		
		config = getConfig();
		
		reloadMessages();
		
		// Listeners
		
		getServer().getPluginManager().registerEvents(new Manager(this), this);
		getServer().getPluginManager().registerEvents(new FreezeListeners(new Manager(this)), this);
		getServer().getPluginManager().registerEvents(new VanishListener(new Manager(this)), this);
		
		// Commands
		
		getCommand("freeze").setExecutor(new FreezeCommand(new Manager(this)));
		getCommand("clearchat").setExecutor(new ClearChatCommand(new Manager(this)));
		getCommand("vanish").setExecutor(new VanishCommand(new Manager(this)));
	}
		
	@Override
	public void onDisable(){
	}
	
	public FileConfiguration getDataConfig() {
		if (messages == null) {
			reloadMessages();
		}
		return messages;
	}

	public void saveMessagesConfig() {
		if (messages == null || messageFile == null) {
			return;
		}
		try {
			getDataConfig().save(messageFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + messageFile, ex);
		}
	}
	
	public void reloadMessages() {
		if (messageFile == null) {
			messageFile = new File(getDataFolder(), "messages.yml");
		}

		messages = YamlConfiguration.loadConfiguration(messageFile);

		Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
			if (defConfigStream != null) {
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				messages.setDefaults(defConfig);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void saveDefaultMessages() {
	    if (messageFile == null) {
	    	messageFile = new File(getDataFolder(), "messages.yml");
	    }
	    if (!messageFile.exists()) {            
	         saveResource("messages.yml", false);
	     }
	}	
	
	/*public void loadData(){
		getDataConfig().set("prefix", getDataConfig().get("prefix"));
		getDataConfig().set("staffChatPrefix", getDataConfig().get("staffChatPrefix"));
		getDataConfig().set("noPermission", getDataConfig().get("noPermission"));
		getDataConfig().set("playerNotFound", getDataConfig().get("playerNotFound"));
		getDataConfig().set("specifyPlayer", getDataConfig().get("specifyPlayer"));
		getDataConfig().set("frozenPlayer", getDataConfig().get("frozenPlayer"));
		getDataConfig().set("onFrozenPlayerMove", getDataConfig().get("onFrozenPlayerMove"));
		getDataConfig().set("beenFrozen", getDataConfig().get("beenFrozen"));
		getDataConfig().set("unfrozenPlayer", getDataConfig().get("unfrozenPlayer"));
		getDataConfig().set("beenUnfrozen", getDataConfig().get("beenUnfrozen"));
		getDataConfig().set("muteChat", getDataConfig().get("muteChat"));
		getDataConfig().set("chatMutedMessage", getDataConfig().get("chatMutedMessage"));
		getDataConfig().set("clearChat", getDataConfig().get("clearChat"));
		getDataConfig().set("staffLogin", getDataConfig().get("staffLogin"));
		getDataConfig().set("staffLeave", getDataConfig().get("staffLeave"));
		getDataConfig().set("silentChestOpen", getDataConfig().get("silentChestOpen"));
		getDataConfig().set("vanishEnabled", getDataConfig().get("vanishEnabled"));
		getDataConfig().set("vanishDisabled", getDataConfig().get("vanishDisabled"));
		
		saveMessagesConfig();
		
	}*/

}
