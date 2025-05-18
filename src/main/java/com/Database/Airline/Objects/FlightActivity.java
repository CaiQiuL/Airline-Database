package com.Database.Airline.Objects;

public class FlightActivity {
    private String flightNum;
    private int ticketsSold;

    public FlightActivity(String flightNum, int ticketSold) {
        this.flightNum = flightNum;
        this.ticketsSold = ticketsSold;
    }

    public String getFlightNum() {
        return flightNum;
    }

    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(int ticketSold) {
        this.ticketsSold = ticketsSold;
    }
}
