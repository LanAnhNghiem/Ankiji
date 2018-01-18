package com.jishin.ankiji.model;

/**
 * Created by lana on 16/01/2018.
 */

public class Moji {
    private String AmHan;
    private String CachDocHira;
    private String NghiaTiengViet;
    private String TuTiengNhat;

    public String getAmHan() {
        return AmHan;
    }

    public String getCachDocHira() {
        return CachDocHira;
    }

    public String getNghiaTiengViet() {
        return NghiaTiengViet;
    }

    public String getTuTiengNhat() {
        return TuTiengNhat;
    }

    @Override
    public String toString() {
        return "Moji{" +
                "AmHan='" + AmHan + '\'' +
                ", CachDocHira='" + CachDocHira + '\'' +
                ", NghiaTiengViet='" + NghiaTiengViet + '\'' +
                ", TuTiengNhat='" + TuTiengNhat + '\'' +
                '}';
    }
}
