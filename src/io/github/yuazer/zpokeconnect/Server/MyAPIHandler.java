package io.github.yuazer.zpokeconnect.Server;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.yuazer.zpokeconnect.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MyAPIHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            // 解析请求参数
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQueryParams(query);
            String personName = queryParams.get("personName");
            personName = URLEncoder.encode(personName, "UTF-8");
            // 判断参数是否符合格式
            if (!personName.matches("^[a-zA-Z0-9_]+$")) {
                // 参数格式不正确，返回错误信息
                errorReturn(exchange);
            }
            FileConfiguration conf = Main.getInstance().getConfig();
            String papimodule = queryParams.get("papiName");
            if (!conf.getConfigurationSection("PAPISetting").getKeys(false).contains(papimodule)) {
                // 参数格式不正确，返回错误信息
                errorReturn(exchange);
                return;
            }
            JsonObject json = new JsonObject();
            Charset codeTyoe = Charset.forName(Main.getInstance().getConfig().getString("GlobalSetting.CodeType"));
            // 获取参数值
            Player player = Bukkit.getPlayer(personName);
            if (player==null||!player.isOnline()) {
                File file = new File("plugins/ZPokeConnect/OfflineSave/" + personName + ".yml");
                YamlConfiguration offlineConf = YamlConfiguration.loadConfiguration(file);
                for (String papi : offlineConf.getConfigurationSection(papimodule).getKeys(false)) {
                    json.addProperty(papi, offlineConf.getString(papimodule+"."+papi));
                }
            } else {
                for (String papi : conf.getConfigurationSection("PAPISetting." + papimodule).getKeys(false)) {
                    json.addProperty(papi, PlaceholderAPI.setPlaceholders(player, conf.getString("PAPISetting." + papimodule + "." + papi)));
                }
            }
            // 设置响应头和响应体
            String jsonResponse = json.toString();
            exchange.getResponseHeaders().set("Content-Type", "application/json;charset=utf-8");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes(codeTyoe).length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(jsonResponse.getBytes(codeTyoe));
            outputStream.flush();
            outputStream.close();
            exchange.close();
        }
    }

    private void errorReturn(HttpExchange exchange) throws IOException {
        String errorMessage = "参数格式不正确";
        byte[] errorBytes = errorMessage.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(400, errorBytes.length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(errorBytes);
        responseBody.close();
    }

    // 解析请求参数
    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] parts = param.split("=");
                if (parts.length == 2) {
                    queryParams.put(parts[0], parts[1]);
                }
            }
        }
        return queryParams;
    }
}
