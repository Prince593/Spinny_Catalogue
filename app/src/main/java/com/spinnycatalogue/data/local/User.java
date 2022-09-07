package com.spinnycatalogue.data.local;


import com.google.firebase.Timestamp;

public class User {
    private String uid = "";
    private Timestamp created_time;
    private String name = "";
    private String email = "";


    public User() {
        uid = "";
        name = "Guest User";
        email = "";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Timestamp getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Timestamp created_time) {
        this.created_time = created_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
