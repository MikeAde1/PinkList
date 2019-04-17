package com.example.mike.pinklist.models;

/**
 * Created by Mike on 11/13/2017.
 */

public class ProfileEdit {
    public ProfileEdit(){
        }

    public String getPhotoURl() {
        return photoURl;
    }

    public void setPhotoURl(String photoURl) {
        this.photoURl = photoURl;
    }

    String photoURl;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    String email,names;
}
