package com.Database.Airline.Objects;

public class CustomerRevenue {
    private int userID;
    private String fname;
    private String lname;
    private float revenue;

    public CustomerRevenue(int userID, String fname, String lname, float revenue) {
        this.userID = userID;
        this.fname = fname;
        this.lname = lname;
        this.revenue = revenue;
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

    public float getRevenue() {
        return revenue;
    }

    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }
}
