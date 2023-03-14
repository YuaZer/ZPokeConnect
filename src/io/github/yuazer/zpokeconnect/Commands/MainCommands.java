package io.github.yuazer.zpokeconnect.Commands;

import io.github.yuazer.zpokeconnect.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("zpokeconnect")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§b/zpokeconnect §a简写-> §b/zpcon");
                sender.sendMessage("§b/zpokeconnect reload §a重载配置文件");
                return true;
            }
            if (args[0].equalsIgnoreCase("reload") && sender.isOp()) {
                Main.getInstance().reloadConfig();
                sender.sendMessage(Main.getInstance().getConfig().getString("Message.reload").replace("&", "§"));
                return true;
            }
        }
        return false;
    }
}
