package com.jishin.ankiji.model;

/**
 * Created by LanAnh on 11/11/2017.
 */

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String linkPhoto;

    public User(String id, String username, String email, String password, String linkPhoto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.linkPhoto = linkPhoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkPhoto() {
        return linkPhoto;
    }

    public void setLinkPhoto(String linkPhoto) {
        this.linkPhoto = linkPhoto;
    }
}
