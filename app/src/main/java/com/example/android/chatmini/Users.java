package com.example.android.chatmini;

public class Users {

    public String name;
    public String desig;
    public String profile_pic;

    public Users(String name, String desig, String profile_pic) {
        this.name = name;
        this.desig = desig;
        this.profile_pic = profile_pic;
    }

    public Users() {
        //just for safety from app crashing
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

    public String getProfile_pic() {
        return this.profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
