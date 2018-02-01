package com.jishin.ankiji.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;

import com.jishin.ankiji.converter.KanjiConverter;

import java.util.ArrayList;

/**
 * Created by lana on 01/02/2018.
 */
@Entity(tableName = "kanji-set")
public class KanjiSet extends Set{
    @TypeConverters({KanjiConverter.class})
    private ArrayList<Kanji> list;
    public KanjiSet(String id, String name, String datetime, int type, ArrayList<Kanji> list) {
        super(id, name, datetime, type);
        this.list = list;
    }

    public ArrayList<Kanji> getList() {
        return list;
    }

    public void setList(ArrayList<Kanji> list) {
        this.list = list;
    }
}
