package com.kadirkertis.domain.interactor.auth.model;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public class User {

    private String uid ;
    private String name;
    private String email;
    private long registrationDate;

    public User() {
    }

    public User(String uid, String name, String email, long registrationDate) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.registrationDate = registrationDate;
    }

    public String getName() {
        return name;
    }
    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public long getRegistrationDate(){
        return registrationDate;
    }
}
