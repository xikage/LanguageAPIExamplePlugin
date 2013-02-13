/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.xhawk87.LanguageAPIExamplePlugin;

import me.xhawk87.LanguageAPI.Language;
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
 * @author Dan
 */
public class LanguageAPIExamplePlugin extends JavaPlugin implements Listener {

    private Language language;

    @Override
    public void onEnable() {
        language = new Language(this);

        getLogger().info(language.get("enabled", "LanguageAPIExamplePlugin has been enabled"));

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(language.get("welcome", "Welcome to the server, {0}", player.getDisplayName()));
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
                    player.sendMessage(language.get("wave-invalid-player", "{0} is not on the server", args[0]));
                    return true;
                }
                Bukkit.broadcastMessage(language.get("wave", "{0} waves to {1}", player.getDisplayName(), target.getDisplayName()));
                return true;
            }
        }
        return false;
    }
}
