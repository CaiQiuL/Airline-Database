package com.Database.Airline.Objects;

import java.util.List;

public class FlightTables {
    private final List<Flight> outboundFlights;
    private final List<Flight> returnFlights;

    public FlightTables(List<Flight> outbound, List<Flight> inbound) {
        this.outboundFlights = outbound;
        this.returnFlights   = inbound;
    }

    public List<Flight> getOutboundFlights() {
        return outboundFlights;
    }

    public List<Flight> getReturnFlights() {
        return returnFlights;
    }
}
