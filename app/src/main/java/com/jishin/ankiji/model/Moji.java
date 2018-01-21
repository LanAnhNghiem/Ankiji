package com.jishin.ankiji.model;

import java.io.Serializable;

/**
 * Created by lana on 16/01/2018.
 */
@SuppressWarnings("serial")
public class Moji implements Serializable{
    private String AmHan;
    private String CachDocHira;
    private String NghiaTiengViet;
    private String TuTiengNhat;

    public Moji(String amHan, String cachDocHira, String nghiaTiengViet, String tuTiengNhat) {
        AmHan = amHan;
        CachDocHira = cachDocHira;
        NghiaTiengViet = nghiaTiengViet;
        TuTiengNhat = tuTiengNhat;
    }
    public Moji() {

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
