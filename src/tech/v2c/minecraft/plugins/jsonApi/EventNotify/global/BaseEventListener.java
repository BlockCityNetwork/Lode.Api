package tech.v2c.minecraft.plugins.jsonApi.EventNotify.global;

import org.bukkit.Server;
import tech.v2c.minecraft.plugins.jsonApi.JsonApi;

public class BaseEventListener {
    public final Server server;
    public BaseEventListener(){
        this.server = JsonApi.instance.getServer();
    }
}
