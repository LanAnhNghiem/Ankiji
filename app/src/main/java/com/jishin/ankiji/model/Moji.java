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
        this.AmHan = amHan;
        this.CachDocHira = cachDocHira;
        this.NghiaTiengViet = nghiaTiengViet;
        this.TuTiengNhat = tuTiengNhat;
    }
    public Moji() {

    }

    public String getAmHan() {
        return AmHan;
    }
      
    public void setAmHan(String amHan) {
        this.AmHan = amHan;
    }
    public String getCachDocHira() {
        return CachDocHira;
    }

    public void setCachDocHira(String cachDocHira) {
        this.CachDocHira = cachDocHira;
    }
    public String getNghiaTiengViet() {
        return NghiaTiengViet;
    }

    public void setNghiaTiengViet(String nghiaTiengViet) {
        this.NghiaTiengViet = nghiaTiengViet;
    }
    public String getTuTiengNhat() {
        return TuTiengNhat;
    }
    public void setTuTiengNhat(String tuTiengNhat) {
        this.TuTiengNhat = tuTiengNhat;
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
