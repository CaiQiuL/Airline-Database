package com.Database.Airline.Objects;

import java.time.LocalDateTime;

public class Flight {

    private String flightNum;
    private boolean isDomestic;
    private LocalDateTime arrivalTime;
    private LocalDateTime departTime;
    private String origin;
    private String destination;
    private int numStops;
    private String airlineID;
    private String craftNum;
    private String airportId;
    private String airportIdDest;
    private int airtime;
    private float price;

    public Flight(String flight_num, boolean isDomestic, LocalDateTime arrivalTime, LocalDateTime departTime, String origin, String destination, int numStops, String airlineID, String craftNum, String airportId, String airportIdDest, int airtime,float price) {
        this.flightNum = flight_num;
        this.isDomestic = isDomestic;
        this.arrivalTime = arrivalTime;
        this.departTime = departTime;
        this.origin = origin;
        this.destination = destination;
        this.numStops = numStops;
        this.airlineID = airlineID;
        this.craftNum = craftNum;
        this.airportId = airportId;
        this.airportIdDest = airportIdDest;
        this.airtime = airtime;
        this.price = price;
    }

    public Flight() {
    }

    public String getFlightNum() {
        return flightNum;
    }

    public void setFlightNum(String flight_num) {
        this.flightNum = flight_num;
    }

    public boolean getisDomestic() {
        return isDomestic;
    }

    public void setisDomestic(boolean domestic) {
        isDomestic = domestic;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrival_time) {
        this.arrivalTime = arrival_time;
    }

    public LocalDateTime getDepartTime() {
        return departTime;
    }

    public void setDepartTime(LocalDateTime depart_time) {
        this.departTime = depart_time;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getNumStops() {
        return numStops;
    }

    public void setNumStops(int num_stops) {
        this.numStops = num_stops;
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

    public void setCraftNum(String craft_num) {
        this.craftNum = craft_num;
    }

    public String getAirportId() {
        return airportId;
    }

    public void setAirportId(String airportId) {
        this.airportId = airportId;
    }

    public String getAirportIdDest() {
        return airportIdDest;
    }

    public void setAirportIdDest(String airportId_dest) {
        this.airportIdDest = airportId_dest;
    }

    public int getAirtime() {
        return airtime;
    }

    public void setAirtime(int airtime) {
        this.airtime = airtime;
    }

    public boolean isDomestic() {
        return isDomestic;
    }

    public void setDomestic(boolean domestic) {
        isDomestic = domestic;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
