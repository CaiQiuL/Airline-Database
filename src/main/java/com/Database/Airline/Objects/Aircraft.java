package com.Database.Airline.Objects;

public class Aircraft {
    private String craftNum;
    private String operationDays;
    private int seats;
    private String airlineID;

    public Aircraft() {
    }

    public Aircraft(String craftNum, String operationDays, int seats, String airlineID) {
        this.craftNum = craftNum;
        this.operationDays = operationDays;
        this.seats = seats;
        this.airlineID = airlineID;
    }

    public String getOperationDays() {
        return operationDays;
    }

    public void setOperationDays(String operationDays) {
        this.operationDays = operationDays;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getAirlineID() {
        return airlineID;
    }

    public void setAirlineID(String airlineID) {
        this.airlineID = airlineID;
    }

    public String getCraftNum() {
        return craftNum;
    }

    public void setCraftNum(String craftNum) {
        this.craftNum = craftNum;
    }
}
