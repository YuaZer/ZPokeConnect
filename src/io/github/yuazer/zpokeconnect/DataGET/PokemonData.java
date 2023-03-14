package io.github.yuazer.zpokeconnect.DataGET;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PokemonData {
    public static String getPokemonName(String playerName, int slot) {
        Player player = Bukkit.getPlayer(playerName);
        Pokemon pokemon = Pixelmon.storageManager.getParty(player.getUniqueId()).get(slot - 1);
        if (pokemon != null) {
            return pokemon.getDisplayName();
        }
        return "无精灵!";
    }

    public static String getPokemonLevel(String playerName, int slot) {
        Player player = Bukkit.getPlayer(playerName);
        Pokemon pokemon = Pixelmon.storageManager.getParty(player.getUniqueId()).get(slot - 1);
        if (pokemon != null) {
            return String.valueOf(pokemon.getLevel());
        }
        return "无精灵!";
    }

    public static String getMove(String playerName, int slot, int move) {
        UUID player = Bukkit.getPlayer(playerName).getUniqueId();
        Pokemon pokemon = Pixelmon.storageManager.getParty(player).get(slot);
        if (pokemon != null && pokemon.getMoveset().get(move) != null) {
            return pokemon.getMoveset().get(move).getMove().getLocalizedName();
        } else {
            return "空";
        }
    }
}
