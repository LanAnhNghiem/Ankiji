package com.jishin.ankiji.model;

import java.io.Serializable;

/**
 * Created by lana on 16/01/2018.
 */
@SuppressWarnings("serial")
public class Moji implements Serializable{
    private String amHan;
    private String cachDocHira;
    private String nghiaTiengViet;
    private String tuTiengNhat;

    public Moji(String amHan, String cachDocHira, String nghiaTiengViet, String tuTiengNhat) {
        this.amHan = amHan;
        this.cachDocHira = cachDocHira;
        this.nghiaTiengViet = nghiaTiengViet;
        this.tuTiengNhat = tuTiengNhat;
    }
    public Moji() {

    }

    public String getAmHan() {
        return amHan;
    }
      
    public void setAmHan(String amHan) {
        this.amHan = amHan;
    }
    public String getCachDocHira() {
        return cachDocHira;
    }

    public void setCachDocHira(String cachDocHira) {
        this.cachDocHira = cachDocHira;
    }
    public String getNghiaTiengViet() {
        return nghiaTiengViet;
    }

    public void setNghiaTiengViet(String nghiaTiengViet) {
        this.nghiaTiengViet = nghiaTiengViet;
    }
    public String getTuTiengNhat() {
        return tuTiengNhat;
    }
    public void setTuTiengNhat(String tuTiengNhat) {
        this.tuTiengNhat = tuTiengNhat;
    }
    @Override
    public String toString() {
        return "Moji{" +
                "amHan='" + amHan + '\'' +
                ", cachDocHira='" + cachDocHira + '\'' +
                ", nghiaTiengViet='" + nghiaTiengViet + '\'' +
                ", tuTiengNhat='" + tuTiengNhat + '\'' +
                '}';
    }
}
