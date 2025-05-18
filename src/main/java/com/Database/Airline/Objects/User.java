package com.Database.Airline.Objects;

public class User {
    private String username;
    private String password;
    private int userID;
    private String fname;
    private String lname;
    private String email;
    private String phoneNum;
    private Boolean isRep;
    private Boolean isAdmin;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public User(String username, String password, int userID, String fname, String lname, String email, String phoneNum, Boolean isAdmin, Boolean isRep) {
        this.username = username;
        this.password = password;
        this.userID = userID;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phoneNum = phoneNum;
        this.isAdmin = isAdmin;
        this.isRep = isRep;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Boolean getRep() {
        return isRep;
    }

    public void setRep(Boolean rep) {
        isRep = rep;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
