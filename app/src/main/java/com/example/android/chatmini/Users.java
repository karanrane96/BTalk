package com.example.android.chatmini;

public class Users {

    public String name, desig, profile_pic,company, email, thumbnail;

    public Users() {
        //just for safety from app crashing
    }

    public Users(String name, String desig, String profile_pic, String company, String email, String thumbnail) {
        this.name = name;
        this.desig = desig;
        this.profile_pic = profile_pic;
        this.company = company;
        this.email = email;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesig() {
        return this.desig;
    }

    public void setDesig(String desig) {
        this.desig = desig;
    }

    public String getCompany() {

        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_pic() {
        return this.profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
