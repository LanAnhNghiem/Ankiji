package com.jishin.ankiji.model;

/**
 * Created by SPlayer on 20/01/2018.
 */

import java.io.Serializable;

/**
 * Created by lana on 19/01/2018.
 */

public class Set implements Serializable{
    private String id;
    private String name;
    private String datetime;

    public Set(String id, String name, String datetime) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
    }

    public Set() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

}
