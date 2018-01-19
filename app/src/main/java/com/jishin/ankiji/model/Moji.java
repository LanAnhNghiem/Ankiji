package com.jishin.ankiji.model;

/**
 * Created by trungnguyeen on 1/19/18.
 */

public class Moji {
    private String id;
    private String AmHan;
    private String CachDocHira;
    private String NghiaTiengViet;
    private String TuTiengNhat;

    public Moji() {
    }

    public Moji(String id, String amHan, String cachDocHira, String nghiaTiengViet, String tuTiengNhat) {
        this.id = id;
        AmHan = amHan;
        CachDocHira = cachDocHira;
        NghiaTiengViet = nghiaTiengViet;
        TuTiengNhat = tuTiengNhat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmHan() {
        return AmHan;
    }

    public void setAmHan(String amHan) {
        AmHan = amHan;
    }

    public String getCachDocHira() {
        return CachDocHira;
    }

    public void setCachDocHira(String cachDocHira) {
        CachDocHira = cachDocHira;
    }

    public String getNghiaTiengViet() {
        return NghiaTiengViet;
    }

    public void setNghiaTiengViet(String nghiaTiengViet) {
        NghiaTiengViet = nghiaTiengViet;
    }

    public String getTuTiengNhat() {
        return TuTiengNhat;
    }

    public void setTuTiengNhat(String tuTiengNhat) {
        TuTiengNhat = tuTiengNhat;
    }
}
