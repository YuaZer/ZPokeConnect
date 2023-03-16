package io.github.yuazer.zpokeconnect.Modules;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AsyncUtils {
    public static <T extends Listener> void listen(Plugin plugin, Listener listener) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().registerEvents(listener, plugin);
            }
        }.runTaskAsynchronously(plugin);
    }
}
