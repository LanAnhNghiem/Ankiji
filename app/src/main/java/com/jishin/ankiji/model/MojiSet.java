package com.jishin.ankiji.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;

import com.jishin.ankiji.converter.MojiConverter;

import java.util.ArrayList;

/**
 * Created by lana on 01/02/2018.
 */
@Entity(tableName = "moji-set")
public class MojiSet extends Set {
    @TypeConverters({MojiConverter.class})
    private ArrayList<Moji> list;
    public MojiSet(String id, String name, String datetime, int type, ArrayList<Moji> list) {
        super(id, name, datetime, type);
        this.list = list;
    }

    public ArrayList<Moji> getList() {
        return list;
    }

    public void setList(ArrayList<Moji> list) {
        this.list = list;
    }
}
