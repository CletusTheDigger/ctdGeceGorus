package com.cletusthedigger.gecegorus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OyuncuListener implements Listener {

    private final CtdGeceGorus plugin;

    public OyuncuListener(CtdGeceGorus plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void oyuncuGiris(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.isGeceGorusAcik(player)) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline() && plugin.isGeceGorusAcik(player)) {
                    plugin.geceGorusVer(player);
                }
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void oyuncuRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (plugin.isGeceGorusAcik(player)) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline() && plugin.isGeceGorusAcik(player)) {
                    plugin.geceGorusVer(player);
                }
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void oyuncuCikis(PlayerQuitEvent event) {
        plugin.oyuncuCikti(event.getPlayer().getUniqueId());
    }
}
