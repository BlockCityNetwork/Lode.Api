package tech.v2c.minecraft.plugins.jsonApi.tools.gameUtils;

import org.bukkit.entity.Player;
import tech.v2c.minecraft.plugins.jsonApi.JsonApi;

import java.util.Collection;

public class UserUtils {
    public static Player GetPlayerByName(String name){
        Collection<? extends Player> onlinePlayers = JsonApi.instance.getServer().getOnlinePlayers();
        Player player = null;
        for (Player item : onlinePlayers) {
            if(item.getName().equalsIgnoreCase(name)){
                player = item;
                break;
            }
        }

        return player;
    }
}
