package io.github.yuazer.zpokeconnect.Server;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.yuazer.zpokeconnect.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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
            if (!personName.matches("^[a-zA-Z0-9]+$")) {
                // 参数格式不正确，返回错误信息
                String errorMessage = "参数格式不正确";
                byte[] errorBytes = errorMessage.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(400, errorBytes.length);
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(errorBytes);
                responseBody.close();
                return;
            }
            String papi = queryParams.get("papiName");
            // 获取参数值
            Player player = Bukkit.getPlayer(personName);
            JsonObject json = new JsonObject();
            if (player == null || !player.isOnline()) {
                json.addProperty("error", "该玩家不在线或不存在!");
            } else {
                FileConfiguration conf = Main.getInstance().getConfig();
                String key = PlaceholderAPI.setPlaceholders(player, "%" + papi + "%");
                json.addProperty(conf.getString("GlobalSetting.papi"), key);
            }
            // 设置响应头和响应体
            String jsonResponse = json.toString();
            exchange.getResponseHeaders().set("Content-Type", "application/json;charset=utf-8");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes(StandardCharsets.UTF_8).length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
            exchange.close();
        }
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
