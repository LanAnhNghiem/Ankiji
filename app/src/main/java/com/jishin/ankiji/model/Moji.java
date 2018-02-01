package com.jishin.ankiji.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by lana on 16/01/2018.
 */
@Entity(tableName = "moji")
@SuppressWarnings("serial")
public class Moji implements Serializable{
    @PrimaryKey
    @NonNull
    private String id;
    private String amHan;
    private String cachDocHira;
    private String nghiaTiengViet;
    private String tuTiengNhat;
    @Ignore
    public Moji(String amHan, String cachDocHira, String nghiaTiengViet, String tuTiengNhat) {
        this.amHan = amHan;
        this.cachDocHira = cachDocHira;
        this.nghiaTiengViet = nghiaTiengViet;
        this.tuTiengNhat = tuTiengNhat;
    }
    public Moji(String id,String amHan, String cachDocHira, String nghiaTiengViet, String tuTiengNhat) {
        this.id = id;
        this.amHan = amHan;
        this.cachDocHira = cachDocHira;
        this.nghiaTiengViet = nghiaTiengViet;
        this.tuTiengNhat = tuTiengNhat;
    }
    @Ignore
    public Moji() {

    }
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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
