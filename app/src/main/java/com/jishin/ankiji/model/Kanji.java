package com.jishin.ankiji.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by lana on 16/01/2018.
 */
@Entity(tableName = "kanji")
@SuppressWarnings("serial")
public class Kanji implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String amhan;
    private String kanji;
    private String tuvung;
    @Ignore
    public Kanji(String id, String amhan, String kanji, String tuvung) {
        this.id = id;
        this.amhan = amhan;
        this.kanji = kanji;
        this.tuvung = tuvung;
    }
    public Kanji(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmhan() {
        return amhan;
    }

    public void setAmhan(String amhan) {
        this.amhan = amhan;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getTuvung() {
        return tuvung;
    }

    public void setTuvung(String tuvung) {
        this.tuvung = tuvung;
    }
}
