package tech.v2c.minecraft.plugins.jsonApi.RESTful.global.entities.server;

import java.util.Date;

public class BanEntryDTO {
    private String name;
    private String reason;
    private Date expires;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
