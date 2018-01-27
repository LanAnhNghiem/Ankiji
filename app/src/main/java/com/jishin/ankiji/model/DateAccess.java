package com.jishin.ankiji.model;

/**
 * Created by SPlayer on 24/01/2018.
 */

public class DateAccess {
    public DateAccess(String type, String id, String date) {
        this.type = type;
        this.id = id;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String id;

    public DateAccess() {
    }

    private String date;
}
