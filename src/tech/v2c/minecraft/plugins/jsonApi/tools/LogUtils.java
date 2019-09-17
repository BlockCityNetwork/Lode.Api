package tech.v2c.minecraft.plugins.jsonApi.tools;

import tech.v2c.minecraft.plugins.jsonApi.JsonApi;

import java.util.logging.Level;

public class LogUtils {
    public static void Log(Level level, String msg){
        JsonApi.instance.getLogger().log(level, msg);
    }

    public static void Debug(String msg){
        if(JsonApi.instance.isDebugMode){
            JsonApi.instance.getLogger().info(msg);
        }
    }

    public static void Info(String msg){
        JsonApi.instance.getLogger().info(msg);
    }
}