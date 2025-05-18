package com.Database.Airline.Objects;

public class Airport {
    private String airportId;
    private String airportName;

    public Airport(String airportID, String airportName) {
        this.airportId = airportID;
        this.airportName = airportName;
    }

    public Airport() {
    }

    public String getAirportId() {
        return airportId;
    }

    public void setAirportId(String airportId) {
        this.airportId = airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }
}
