package com.jishin.ankiji.model;

/**
 * Created by huuduc on 07/12/2017.
 */

public class DataKanji{
    private String tuvung;
    private String kanji;

    private String amhan;
    private String id;



    public DataKanji() {
    }

    public DataKanji(String amhan, String id, String kanji, String tuvung) {
        this.amhan = amhan;
        this.id = id;
        this.kanji = kanji;
        this.tuvung = tuvung;
    }

    public String getAmHan() {
        return amhan;
    }

    public void setAmHan(String amhan) {
        this.amhan = amhan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getTuVung() {
        return tuvung;
    }

    public void setTuVung(String tuvung) {
        this.tuvung = tuvung;
    }
}
