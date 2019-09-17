package tech.v2c.minecraft.plugins.jsonApi.RESTful.actions;

import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.FileUtil;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.BaseAction;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.annotations.ApiRoute;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.entities.JsonData;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.entities.server.PluginDTO;
import tech.v2c.minecraft.plugins.jsonApi.tools.results.JsonResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class PluginAction extends BaseAction {
    private final PluginManager pluginManager;

    public PluginAction(){
        pluginManager = server.getPluginManager();
    }

    // 获取插件列表
    @ApiRoute(Path="/api/Plugin/GetList")
    public JsonResult GetPluginList(){
        ArrayList<PluginDTO> pluginList = new ArrayList<PluginDTO>();

        Plugin[] plugins = pluginManager.getPlugins();
        for (Plugin plugin : plugins) {
            PluginDTO plg = new PluginDTO();
            plg.setName(plugin.getName());
            plg.setVersion(plugin.getDescription().getVersion());
            plg.setWebsite(plugin.getDescription().getWebsite());
            plg.setEnabled(plugin.isEnabled());
            plg.setDisabled(!plugin.isEnabled());
            plg.setAuthors(plugin.getDescription().getAuthors());
            plg.setDescription(plugin.getDescription().getDescription());

            pluginList.add(plg);
        }

        return new JsonResult(pluginList);
    }

    @ApiRoute(Path="/api/Plugin/GetPluginInfo")
    public JsonResult GetPluginInfo(JsonData data){
        Plugin plugin = pluginManager.getPlugin(data.Data.get("name").toString());

        if(plugin == null) return new JsonResult(null, 404, "Error: plugin not found.");

        PluginDTO plg = new PluginDTO();
        plg.setName(plugin.getName());
        plg.setVersion(plugin.getDescription().getVersion());
        plg.setWebsite(plugin.getDescription().getWebsite());
        plg.setEnabled(plugin.isEnabled());
        plg.setDisabled(!plugin.isEnabled());
        plg.setAuthors(plugin.getDescription().getAuthors());
        plg.setDescription(plugin.getDescription().getDescription());
        
        return new JsonResult(plg);
    }

    // 关闭指定插件
    @ApiRoute(Path="/api/Plugin/Disable")
    public JsonResult DisablePluginByName(JsonData data){
        Plugin plugin = pluginManager.getPlugin(data.Data.get("name").toString());

        if(plugin == null) return new JsonResult(null, 404, "Error: plugin not found.");

        if(!pluginManager.isPluginEnabled(plugin)){
            return new JsonResult(null, 401, "Plugin is disable alright.");
        }

        pluginManager.disablePlugin(plugin);
        return GetPluginList();
    }

    // 开启指定插件
    @ApiRoute(Path="/api/Plugin/Enable")
    public JsonResult EnablePluginByName(JsonData data){
        Plugin plugin = pluginManager.getPlugin(data.Data.get("name").toString());

        if(plugin == null) return new JsonResult(null, 404, "Error: plugin not found.");

        pluginManager.enablePlugin(plugin);

        return GetPluginList();
    }

    // 关闭所有插件
    @ApiRoute(Path="/api/Plugin/Disable/All")
    public JsonResult DisableAllPlugins(){
        pluginManager.disablePlugins();

        return GetPluginList();
    }

    // 安装插件
    @ApiRoute(Path="/api/Plugin/Install")
    public JsonResult InstallPlugin(JsonData data){
        Map<String, File> allFile = ( Map<String, File>)data.Data.get("files");
        String pluginPath = "./plugins/";

        for (Map.Entry<String, File> plg : allFile.entrySet()) {
            final String fileName = plg.getKey();
            final File file = plg.getValue();
            final File outFile = new File(pluginPath + fileName);

            try {
                Files.copy(file.toPath(), outFile.toPath());
                Plugin uploadPlg = null;
                uploadPlg = pluginManager.loadPlugin(outFile);
                pluginManager.enablePlugin(uploadPlg);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidDescriptionException e) {
                e.printStackTrace();
            } catch (InvalidPluginException e) {
                e.printStackTrace();
            }
        }

        return GetPluginList();
    }
}
