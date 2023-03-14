package io.github.yuazer.zpokeconnect;

import com.sun.net.httpserver.HttpServer;
import io.github.yuazer.zpokeconnect.Commands.MainCommands;
import io.github.yuazer.zpokeconnect.Server.MyAPIHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main extends JavaPlugin {
    private static Main instance;
    private HttpServer server;

    public static Main getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        logLoaded(this);
        saveDefaultConfig();
        try {
            server = HttpServer.create(new InetSocketAddress(Main.getInstance().getConfig().getInt("ServerSetting.port")), 0);
            // 将您的API绑定到路由 /pixelmon 上
            server.createContext("/pixelmon", new MyAPIHandler());
            // 启动服务器
            server.start();
            Main.getInstance().getLogger().info(String.format("§aHTTP服务器已经在端口:§b%s§a上开启", Main.getInstance().getConfig().getInt("ServerSetting.port")));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ServerLoad serverLoad = new ServerLoad();
//        serverLoad.serverLoad(Main.getInstance().getConfig().getInt("ServerSetting.port"));
    }

    public void onDisable() {
        logDisable(this);
        if (server != null) {
            server.stop(0);
        }
//        ServerLoad.getBossGroup().shutdownGracefully();
//        ServerLoad.getWorkerGroup().shutdownGracefully();
    }

    public static void logLoaded(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §f已加载", plugin.getName()));
        Bukkit.getLogger().info("§b作者:§eZ菌");
        Bukkit.getLogger().info("§b版本:§e" + plugin.getDescription().getVersion());
        Bukkit.getPluginCommand("zpokeconnect").setExecutor(new MainCommands());
    }

    public static void logDisable(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §c已卸载", plugin.getName()));
    }
}
