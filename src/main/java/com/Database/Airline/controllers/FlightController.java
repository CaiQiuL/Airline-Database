package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.FlightSearch;
import com.Database.Airline.Objects.Flight;
import com.Database.Airline.Objects.FlightTables;
import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightSearch flightSearch;

    @PostMapping("/search")
    public String searchFlights(
            @RequestParam String flightType,
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String flightClass,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc")   String sortDir,
            HttpSession session,
            Model model
    ) throws SQLException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error", "Please log in first.");
            return "login";
        }

        FlightTables results = flightSearch.searchFlights(flightType, origin, destination);
        List<Flight> outbound = new ArrayList<>(results.getOutboundFlights());
        List<Flight> inbound  = new ArrayList<>(results.getReturnFlights());
        Comparator<Flight> comp;
        switch (sortBy) {
            case "departTime":  comp = Comparator.comparing(Flight::getDepartTime);   break;
            case "arrivalTime": comp = Comparator.comparing(Flight::getArrivalTime);  break;
            case "duration":    comp = Comparator.comparingInt(Flight::getAirtime);    break;
            case "price":
            default:            comp = Comparator.comparingDouble(Flight::getPrice);   break;
        }
        if ("desc".equalsIgnoreCase(sortDir)) {
            comp = comp.reversed();
        }
        outbound.sort(comp);
        inbound.sort(comp);
        model.addAttribute("flightType",      flightType);
        model.addAttribute("origin",          origin);
        model.addAttribute("destination",     destination);
        model.addAttribute("flightClass",     flightClass);
        model.addAttribute("sortBy",          sortBy);
        model.addAttribute("sortDir",         sortDir);
        model.addAttribute("outboundFlights", outbound);
        model.addAttribute("returnFlights",   inbound);
        if ("oneway".equalsIgnoreCase(flightType)) {
            model.addAttribute("flights", outbound);
        }
        return "FlightResults";
    }
}