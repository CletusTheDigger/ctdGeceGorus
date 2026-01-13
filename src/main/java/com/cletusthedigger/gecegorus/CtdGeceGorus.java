package com.cletusthedigger.gecegorus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CtdGeceGorus extends JavaPlugin {

    private static CtdGeceGorus instance;
    private Set<UUID> gecGorusKapali;

    @Override
    public void onEnable() {
        instance = this;
        gecGorusKapali = ConcurrentHashMap.newKeySet();
        saveDefaultConfig();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            String[] logo = {
                    "",
                    ChatColor.YELLOW
                            + "  ####  #####  ####     ####   #####   ####  #####   ####    ###   ####   #   #   ####",
                    ChatColor.YELLOW
                            + " #        #    #   #   #       #      #      #      #       #   #  #   #  #   #  #    ",
                    ChatColor.YELLOW
                            + " #        #    #   #   # ###   ####   #      ####   # ###   #   #  ####   #   #   ### ",
                    ChatColor.YELLOW
                            + " #        #    #   #   #   #   #      #      #      #   #   #   #  #  #   #   #      #",
                    ChatColor.YELLOW
                            + "  ####    #    ####     ####   #####   ####  #####   ####    ###   #   #   ###   #### ",
                    "",
                    ChatColor.DARK_GRAY + "=========================================",
                    ChatColor.WHITE + "  Plugin: " + ChatColor.AQUA + "ctdGeceGorus",
                    ChatColor.WHITE + "  Surum: " + ChatColor.AQUA + getDescription().getVersion(),
                    ChatColor.WHITE + "  Yapimci: " + ChatColor.AQUA + "CletusTheDigger",
                    ChatColor.DARK_GRAY + "=========================================",
                    ""
            };

            for (String line : logo) {
                Bukkit.getConsoleSender().sendMessage(line);
            }
        });

        getServer().getPluginManager().registerEvents(new OyuncuListener(this), this);
        getCommand("ctdgecegorus").setExecutor(new GeceGorusKomut(this));
        getCommand("ctdgecegorus").setTabCompleter(new GeceGorusKomut(this));

        Bukkit.getScheduler().runTaskLater(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (isGeceGorusAcik(player)) {
                    geceGorusVer(player);
                }
            }
        }, 20L);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ctdGeceGorus] Plugin basariyla aktif edildi!");
    }

    @Override
    public void onDisable() {
        if (gecGorusKapali != null) {
            gecGorusKapali.clear();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ctdGeceGorus] Plugin kapatildi!");
    }

    public void reloadPluginConfig() {
        reloadConfig();
    }

    public String getMesaj(String path) {
        String mesaj = getConfig().getString("mesajlar." + path);
        if (mesaj == null) {
            return ChatColor.RED + "Mesaj bulunamadi: " + path;
        }

        String prefix = getConfig().getString("prefix", "&e[ctdGeceGorus] &r");
        mesaj = mesaj.replace("{prefix}", prefix);
        return ChatColor.translateAlternateColorCodes('&', mesaj);
    }

    public static CtdGeceGorus getInstance() {
        return instance;
    }

    public boolean isGeceGorusAcik(Player player) {
        return !gecGorusKapali.contains(player.getUniqueId());
    }

    public void setGeceGorusDurumu(Player player, boolean acik) {
        if (acik) {
            gecGorusKapali.remove(player.getUniqueId());
            geceGorusVer(player);
        } else {
            gecGorusKapali.add(player.getUniqueId());
            geceGorusKaldir(player);
        }
    }

    public void geceGorusVer(Player player) {
        if (player == null || !player.isOnline())
            return;
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            return;
        }

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.NIGHT_VISION,
                Integer.MAX_VALUE,
                0,
                false,
                false
        ), true);
    }

    public void geceGorusKaldir(Player player) {
        if (player == null || !player.isOnline())
            return;
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    public void oyuncuCikti(UUID uuid) {
        gecGorusKapali.remove(uuid);
    }
}
