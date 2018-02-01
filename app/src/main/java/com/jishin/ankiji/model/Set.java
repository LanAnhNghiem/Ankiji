package com.jishin.ankiji.model;

/**
 * Created by SPlayer on 20/01/2018.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by lana on 19/01/2018.
 */
@Entity(tableName = "set")
public class Set implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String datetime;
    private int type; //Type = 0 --> Moji, Type = 1 -->Kanji

    public Set(String id, String name, String datetime) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
    }
    public Set(String id, String name, String datetime, int type) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
        this.type = type;
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
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
