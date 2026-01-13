package com.cletusthedigger.gecegorus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeceGorusKomut implements CommandExecutor, TabCompleter {

    private final CtdGeceGorus plugin;
    private static final List<String> ALT_KOMUTLAR;
    static {
        List<String> temp = new ArrayList<>();
        temp.add("ac");
        temp.add("kapat");
        temp.add("reload");
        ALT_KOMUTLAR = Collections.unmodifiableList(temp);
    }

    public GeceGorusKomut(CtdGeceGorus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                plugin.reloadPluginConfig();
                sender.sendMessage("[ctdGeceGorus] Config basariyla yenilendi!");
                return true;
            }
            sender.sendMessage("[ctdGeceGorus] Bu komut (reload haric) sadece oyuncular tarafindan kullanilabilir!");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            String prefix = plugin.getConfig().getString("prefix", "").replace("&", "§");
            player.sendMessage("");
            player.sendMessage(plugin.getMesaj("bilinmeyen-komut"));
            boolean durum = plugin.isGeceGorusAcik(player);
            String durumText = durum ? "§aACIK" : "§cKAPALI";
            player.sendMessage(prefix + "Mevcut durum: " + durumText);
            player.sendMessage("");
            return true;
        }

        String altKomut = args[0].toLowerCase();
        switch (altKomut) {
            case "ac":
            case "aç":
                if (plugin.isGeceGorusAcik(player)) {
                    player.sendMessage(plugin.getMesaj("zaten-acik"));
                } else {
                    plugin.setGeceGorusDurumu(player, true);
                    player.sendMessage(plugin.getMesaj("gece-gorus-acildi"));
                }
                break;
            case "kapat":
                if (!plugin.isGeceGorusAcik(player)) {
                    player.sendMessage(plugin.getMesaj("zaten-kapali"));
                } else {
                    plugin.setGeceGorusDurumu(player, false);
                    player.sendMessage(plugin.getMesaj("gece-gorus-kapatildi"));
                }
                break;
            case "reload":
                if (player.isOp() || player.hasPermission("ctdgecegorus.admin")) {
                    plugin.reloadPluginConfig();
                    player.sendMessage(plugin.getMesaj("reload-basarili"));
                } else {
                    player.sendMessage(plugin.getMesaj("yetki-yok"));
                }
                break;
            default:
                player.sendMessage(plugin.getMesaj("bilinmeyen-komut"));
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }

        String girilen = args[0].toLowerCase();

        if (girilen.isEmpty()) {
            return ALT_KOMUTLAR;
        }

        List<String> sonuc = new ArrayList<>(3);
        for (String altKomut : ALT_KOMUTLAR) {
            if (altKomut.startsWith(girilen)) {
                if (altKomut.equals("reload")) {
                    if (sender.isOp() || sender.hasPermission("ctdgecegorus.admin")) {
                        sonuc.add(altKomut);
                    }
                } else {
                    sonuc.add(altKomut);
                }
            }
        }
        return sonuc;
    }
}
