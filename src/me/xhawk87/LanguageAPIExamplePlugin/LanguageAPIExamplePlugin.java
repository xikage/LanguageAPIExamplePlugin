/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.xhawk87.LanguageAPIExamplePlugin;

import me.xhawk87.LanguageAPI.ISOCode;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author XHawk87
 */
public class LanguageAPIExamplePlugin extends JavaPlugin implements Listener {

    private LanguageWrapper language;

    @Override
    public void onEnable() {
        // Create the language library for this plugin
        language = new LanguageWrapper(this, ISOCode.eng); // English locale - Change it to whatever you code in

        // You can then pull in language specific strings to use anywhere in your code
        // The simplest just require a key (where it should be stored as in the language.yml)
        // And a template, the default text to be displayed
        getLogger().info(language.get(Bukkit.getConsoleSender(), "enabled", "LanguageAPIExamplePlugin has been enabled"));

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // More complex templates can have data inserted, just add them as 
        // additional parameters and include the index of the parameter to use
        // in curly braces {0} to insert the value of the data at that point
        player.sendMessage(language.get(player, "welcome", "Welcome to the server, {0}", player.getDisplayName()));
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("wave")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length != 1) {
                    return false;
                }
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null) {
                    player.sendMessage(language.get(player, "wave-invalid-player", "{0} is not on the server", args[0]));
                    return true;
                }
                // Any number of additional dynamic data items can be inserted 
                // into the template, just so long as there are corresponding
                // data for every curly braced index number
                Bukkit.broadcastMessage(language.get(player, "wave", "{0} waves to {1}", player.getDisplayName(), target.getDisplayName()));
                return true;
            }
        }
        return false;
    }
}
