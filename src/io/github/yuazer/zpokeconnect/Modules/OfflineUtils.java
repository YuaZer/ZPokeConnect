package io.github.yuazer.zpokeconnect.Modules;

import io.github.yuazer.zpokeconnect.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class OfflineUtils {
    public static void savePlayerData(Player player, String moduleName) {
        try {
            File file = new File("plugins/ZPokeConnect/OfflineSave/" + player.getName() + ".yml");
            if (!file.exists()) {
                file.createNewFile();
            }
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
            for (String key : Main.getInstance().getConfig().getConfigurationSection("PAPISetting." + moduleName).getKeys(false)) {
                conf.set(moduleName + "." + key, PlaceholderAPI.setPlaceholders(player, Main.getInstance().getConfig().getString("PAPISetting." + moduleName + "." + key)));
            }
            conf.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
