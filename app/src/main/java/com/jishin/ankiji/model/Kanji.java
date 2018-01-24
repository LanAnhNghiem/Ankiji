package com.jishin.ankiji.model;

import java.io.Serializable;

/**
 * Created by lana on 16/01/2018.
 */
@SuppressWarnings("serial")
public class Kanji implements Serializable {
    private String id;
    private String amhan;
    private String kanji;
    private String tuvung;

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
