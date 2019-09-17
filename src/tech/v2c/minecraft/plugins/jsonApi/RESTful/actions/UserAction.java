package tech.v2c.minecraft.plugins.jsonApi.RESTful.actions;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import tech.v2c.minecraft.plugins.jsonApi.JsonApi;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.BaseAction;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.annotations.ApiRoute;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.entities.JsonData;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.entities.server.BanEntryDTO;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.entities.user.OnlineUserDTO;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.entities.user.PlayerInventoryDTO;
import tech.v2c.minecraft.plugins.jsonApi.RESTful.global.entities.user.UserPositionDTO;
import tech.v2c.minecraft.plugins.jsonApi.tools.results.JsonResult;
import tech.v2c.minecraft.plugins.jsonApi.tools.gameUtils.UserUtils;

import java.util.*;

public class UserAction extends BaseAction {
    @ApiRoute(Path = "/api/User/GetUserByName")
    public JsonResult GetUserByName(JsonData data) {
        String searchName = data.Data.get("name").toString();

        Player user = UserUtils.GetPlayerByName(searchName);
        if (user == null) return new JsonResult(null, 404, "Error: user not found.");

        OnlineUserDTO onlineUser = new OnlineUserDTO();
        onlineUser.setName(user.getName());
        onlineUser.setDisplayName(user.getDisplayName());
        onlineUser.setId(user.getEntityId());
        onlineUser.setUid(user.getUniqueId());
        onlineUser.setGameMode(user.getGameMode().getValue());
        onlineUser.setHeight(user.getHeight());
        onlineUser.setHealth(user.getHealth());
        onlineUser.setMaxHealth(user.getMaxHealth());
        // onlineUser.setPing(user.getAddress().getAddress().);
        onlineUser.setOp(user.isOp());
        onlineUser.setExperience(user.getTotalExperience());
        onlineUser.setExperienceLevel(user.getExpToLevel());

        UserPositionDTO up = new UserPositionDTO();
        up.setX(user.getLocation().getX());
        up.setY(user.getLocation().getY());
        up.setZ(user.getLocation().getZ());

        onlineUser.setPosition(up);

        return new JsonResult(onlineUser);
    }

    @ApiRoute(Path = "/api/User/GetOnlineList")
    public JsonResult GetOnlineUserList() {
        Collection<? extends Player> users = server.getOnlinePlayers();
        ArrayList<OnlineUserDTO> userList = new ArrayList<OnlineUserDTO>();

        for (Player user : users) {
            OnlineUserDTO onlineUser = new OnlineUserDTO();
            onlineUser.setName(user.getName());
            onlineUser.setDisplayName(user.getDisplayName());
            onlineUser.setId(user.getEntityId());
            onlineUser.setUid(user.getUniqueId());
            onlineUser.setGameMode(user.getGameMode().getValue());
            onlineUser.setHeight(user.getHeight());
            onlineUser.setHealth(user.getHealth());
            onlineUser.setMaxHealth(user.getMaxHealth());
            // onlineUser.setPing(user.getPing());
            onlineUser.setOp(user.isOp());
            onlineUser.setExperience(user.getTotalExperience());
            onlineUser.setExperienceLevel(user.getExpToLevel());

            UserPositionDTO up = new UserPositionDTO();
            up.setX(user.getLocation().getX());
            up.setY(user.getLocation().getY());
            up.setZ(user.getLocation().getZ());

            onlineUser.setPosition(up);

            userList.add(onlineUser);
        }

        return new JsonResult(userList);
    }

    @ApiRoute(Path = "/api/User/BanByName")
    public JsonResult BanUserByName(JsonData data) {
        String userName = data.Data.get("name").toString();
        Object reason = data.Data.get("reason");
        Object endTime = data.Data.get("expirationDate");

        server.getBanList(BanList.Type.NAME).addBan(userName, reason == null ? "" : reason.toString(), endTime != null ? new Date(Long.parseLong(endTime.toString())) : null, null);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/BanByIp")
    public JsonResult BanUserByIp(JsonData data) {
        String userIp = data.Data.get("ip").toString();

        server.banIP(userIp);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/RemoveNameBan")
    public JsonResult RemoveNameBan(JsonData data){
        String userName = data.Data.get("target").toString();
        server.getBanList(BanList.Type.NAME).pardon(userName);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/RemoveIpBan")
    public JsonResult RemoveIpBan(JsonData data){
        String ip = data.Data.get("target").toString();
        server.unbanIP(ip);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/GetNameBanList")
    public JsonResult GetNameBanList() {
        List<BanEntryDTO> banList = new ArrayList<BanEntryDTO>();
        server.getBanList(BanList.Type.NAME).getBanEntries().forEach(banUser -> {
            BanEntryDTO banEntry = new BanEntryDTO();

            banEntry.setName(banUser.getTarget());
            banEntry.setReason(banUser.getReason());
            banEntry.setExpires(banUser.getExpiration());

            banList.add(banEntry);
        });

        return new JsonResult(banList);
    }

    @ApiRoute(Path = "/api/User/GetIpBanList")
    public JsonResult GetIpBanList() {
        List<BanEntryDTO> banList = new ArrayList<BanEntryDTO>();

        server.getBanList(BanList.Type.IP).getBanEntries().forEach(banUser -> {
            BanEntryDTO banEntry = new BanEntryDTO();

            banEntry.setName(banUser.getTarget());
            banEntry.setReason(banUser.getReason());
            banEntry.setExpires(banUser.getExpiration());

            banList.add(banEntry);
        });

        return new JsonResult(banList);
    }

    @ApiRoute(Path = "/api/User/GetWhiteList")
    public JsonResult GetWhiteList() {
        ArrayList<String> whiteList = new ArrayList<String>();
        server.getWhitelistedPlayers().forEach(whiteListUser -> {
            whiteList.add(whiteListUser.getName());
        });

        return new JsonResult(whiteList);
    }

    @ApiRoute(Path = "/api/User/AddWhiteList")
    public JsonResult AddWhiteList(JsonData data) {
        String userName = data.Data.get("name").toString();
        OfflinePlayer player = server.getOfflinePlayer(userName);
        player.setWhitelisted(true);
        return GetWhiteList();
    }

    @ApiRoute(Path = "/api/User/RemoveWhiteList")
    public JsonResult RemoveWhiteList(JsonData data) {
        String userName = data.Data.get("name").toString();
        OfflinePlayer player = server.getOfflinePlayer(userName);
        player.setWhitelisted(false);
        return GetWhiteList();
    }

    @ApiRoute(Path = "/api/User/GetOPList")
    public JsonResult GetOpList() {
        ArrayList<String> opList = new ArrayList<String>();
        server.getOperators().forEach(op -> {
            opList.add(op.getName());
        });
        return new JsonResult(opList);
    }

    @ApiRoute(Path = "/api/User/AddOp")
    public JsonResult AddOp(JsonData data) {
        String userName = data.Data.get("name").toString();
        OfflinePlayer player = server.getOfflinePlayer(userName);
        player.setOp(true);

        return GetOpList();
    }

    @ApiRoute(Path = "/api/User/RemoveOp")
    public JsonResult RemoveOp(JsonData data) {
        String userName = data.Data.get("name").toString();
        OfflinePlayer player = server.getOfflinePlayer(userName);
        player.setOp(false);

        return GetOpList();
    }

    @ApiRoute(Path = "/api/User/SetGameMode")
    public JsonResult SetGameMode(JsonData data) {
        String userName = data.Data.get("name").toString();
        int gameMode = (int) Double.parseDouble(data.Data.get("gameMode").toString());
        Player user = UserUtils.GetPlayerByName(userName);
        if (user == null) return new JsonResult(null, 404, "Error: user not found.");

        user.setGameMode(GameMode.getByValue(gameMode));

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/SendChat")
    public JsonResult SendChat(JsonData data) {
        String userName = data.Data.get("name").toString();
        String message = data.Data.get("message").toString();
        Object source = data.Data.get("source");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.chat(message);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/SendMessage")
    public JsonResult SendMessage(JsonData data) {
        String userName = data.Data.get("name").toString();
        String message = data.Data.get("message").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.sendMessage(message);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/SendExperience")
    public JsonResult SendExperience(JsonData data) {
        String userName = data.Data.get("name").toString();
        int expType = (int) Double.parseDouble(data.Data.get("type").toString());
        int value = (int) Double.parseDouble(data.Data.get("value").toString());
        Object msg = data.Data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        if (expType == 0) {
            player.giveExp(value);
        } else {
            player.giveExpLevels(value);
        }

        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/SetPlayerFire")
    public JsonResult SetPlayerFire(JsonData data) {
        String userName = data.Data.get("name").toString();
        int time = (int) Double.parseDouble(data.Data.get("time").toString());
        Object msg = data.Data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.setFireTicks(time);

        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/KillPlayer")
    public JsonResult KillPlayer(JsonData data) {
        String userName = data.Data.get("name").toString();
        Object msg = data.Data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.setHealth(0);

        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    // 无法执行
    @ApiRoute(Path = "/api/User/KickPlayer")
    public JsonResult KickPlayer(JsonData data) {
        String userName = data.Data.get("name").toString();
        boolean isKickByAdmin = (boolean) data.Data.get("isKickByAdmin");
        Object reason = data.Data.get("reason");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.kickPlayer("");

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/ClearPlayerInventory")
    public JsonResult ClearPlayerInventory(JsonData data) {
        String userName = data.Data.get("name").toString();
        Object msg = data.Data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.getInventory().clear();

        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/GetPlayerInventory")
    public JsonResult GetPlayerInventory(JsonData data) {
        String userName = data.Data.get("name").toString();
        ArrayList<PlayerInventoryDTO> list = new ArrayList<PlayerInventoryDTO>();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        PlayerInventory playerInventory = player.getInventory();
        for (int i = 0; i < playerInventory.getSize(); i++) {
            ItemStack item = playerInventory.getItem(i);
            if(item != null){
                if (item.getType() != Material.getMaterial("AIR")) {
                    PlayerInventoryDTO playerInventoryDTO = new PlayerInventoryDTO();
                    playerInventoryDTO.setIndex(i);
                    playerInventoryDTO.setId(item.getType().getId());
                    playerInventoryDTO.setName(item.getType().name());
                    playerInventoryDTO.setCount(item.getAmount());

                    list.add(playerInventoryDTO);
                }
            }
        }

        return new JsonResult(list);
    }

    @ApiRoute(Path = "/api/User/GetInHandItem")
    public JsonResult GetInHandItem(JsonData data) {
        String userName = data.Data.get("name").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        PlayerInventory playerInventory = player.getInventory();

        ItemStack item = playerInventory.getItemInMainHand();
        PlayerInventoryDTO playerInventoryDTO = new PlayerInventoryDTO();
        // playerInventoryDTO.setIndex(playerInventory.getItemInHand().);
        playerInventoryDTO.setId(item.getType().getId());
        playerInventoryDTO.setName(item.getType().name());
        playerInventoryDTO.setCount(item.getAmount());

        return new JsonResult(playerInventoryDTO);
    }

    @ApiRoute(Path = "/api/User/GetPlayerPosition")
    public JsonResult GetPlayerPosition(JsonData data) {
        String userName = data.Data.get("name").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        Location location = player.getLocation();
        UserPositionDTO userPositionDTO = new UserPositionDTO();
        userPositionDTO.setX(location.getX());
        userPositionDTO.setY(location.getY());
        userPositionDTO.setZ(location.getZ());

        return new JsonResult(userPositionDTO);
    }

    // 无法使用
    @ApiRoute(Path = "/api/User/SetPlayerPosition")
    public JsonResult SetPlayerPosition(JsonData data) {
        String userName = data.Data.get("name").toString();
        double x = Double.parseDouble(data.Data.get("x").toString());
        double y = Double.parseDouble(data.Data.get("y").toString());
        double z = Double.parseDouble(data.Data.get("z").toString());

        Object msg = data.Data.get("message");

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.setVelocity(new Location(player.getWorld(),x,y,z).getDirection());
        if (msg != null) {
            player.sendMessage(msg.toString());
        }

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/GetPlayerHealth")
    public JsonResult GetPlayerHealth(JsonData data) {
        String userName = data.Data.get("name").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        return new JsonResult(player.getHealth());
    }

    @ApiRoute(Path = "/api/User/SetPlayerHealth")
    public JsonResult SetPlayerHealth(JsonData data) {
        String userName = data.Data.get("name").toString();
        float healthValue = Float.parseFloat(data.Data.get("value").toString());

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.setHealth(healthValue);

        return new JsonResult();
    }

    @ApiRoute(Path = "/api/User/GetPlayerHunger")
    public JsonResult GetPlayerHunger(JsonData data) {
        String userName = data.Data.get("name").toString();

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        return new JsonResult(player.getFoodLevel());
    }

    @ApiRoute(Path = "/api/User/SetPlayerHunger")
    public JsonResult SetPlayerHunger(JsonData data) {
        String userName = data.Data.get("name").toString();
        int hungerValue = (int) Double.parseDouble(data.Data.get("value").toString());

        Player player = UserUtils.GetPlayerByName(userName);
        if (player == null) return new JsonResult(null, 404, "Error: user not found.");

        player.setFoodLevel(hungerValue);

        return new JsonResult();
    }
}
