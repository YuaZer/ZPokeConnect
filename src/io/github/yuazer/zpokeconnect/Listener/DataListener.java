package io.github.yuazer.zpokeconnect.Listener;

import io.github.yuazer.zpokeconnect.Main;
import io.github.yuazer.zpokeconnect.Modules.OfflineUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataListener implements Listener {
    @EventHandler
    public void onLogOut(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        for (String module: Main.getInstance().getConfig().getStringList("OfflineSaving")){
            OfflineUtils.savePlayerData(player,module);
        }
    }
}
