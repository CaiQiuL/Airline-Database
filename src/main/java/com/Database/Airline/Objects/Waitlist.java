package com.Database.Airline.Objects;

import java.time.LocalDateTime;

public class Waitlist {
    private int waitlist_id;
    private int userID;
    private String flight_num;
    private LocalDateTime timestamp;
    private int position;

    public Waitlist(int waitlist_id, int userID, String flight_num, LocalDateTime timestamp) {
        this.waitlist_id = waitlist_id;
        this.userID = userID;
        this.flight_num = flight_num;
        this.timestamp = timestamp;
    }

    public int getWaitlist_id() {
        return waitlist_id;
    }

    public void setWaitlist_id(int waitlist_id) {
        this.waitlist_id = waitlist_id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFlight_num() {
        return flight_num;
    }

    public void setFlight_num(String flight_num) {
        this.flight_num = flight_num;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
