package com.jishin.ankiji.model;

/**
 * Created by lana on 16/01/2018.
 */

public class Kanji {
    private String id;

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

    public Kanji() {
    }

    private String amhan;
    private String kanji;
    private String tuvung;
}
