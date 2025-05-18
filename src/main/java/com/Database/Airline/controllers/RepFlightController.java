package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.AirportDB;
import com.Database.Airline.DBConnection.FlightDB;
import com.Database.Airline.Objects.Flight;
import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/rep/flight")
public class RepFlightController {
    @Autowired
    private FlightDB flightDB;

    @Autowired
    private AirportDB airportDB;

    private boolean checkRep(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && Boolean.TRUE.equals(u.getRep());
    }

    @GetMapping("/manage")
    public String list(HttpSession session, Model model) throws SQLException {
        if (!checkRep(session)) return "redirect:/login";
        model.addAttribute("flights", flightDB.findAll());
        return "flightList";
    }

    @GetMapping("/create")
    public String createForm(HttpSession session, Model model) {
        if (!checkRep(session)) return "redirect:/login";
        model.addAttribute("flight", new Flight());
        return "flightForm";
    }

    @PostMapping("/create")
    public String doCreate(@ModelAttribute Flight flight, HttpSession session) throws SQLException {
        if (!checkRep(session)) return "redirect:/login";
        long mins = Duration.between(flight.getDepartTime(), flight.getArrivalTime()).toMinutes();
        flight.setAirtime((int)mins);
        flightDB.create(flight);
        return "redirect:/rep/flight/manage";
    }

    @GetMapping("/edit")
    public String editForm(@RequestParam String flightNum,
                           HttpSession session,
                           Model model) throws SQLException {
        if (!checkRep(session)) return "redirect:/login";
        Flight f = flightDB.findById(flightNum);
        if (f == null) throw new IllegalArgumentException("No such flight: " + flightNum);
        model.addAttribute("flight", f);
        return "flightForm";
    }

    @PostMapping("/edit")
    public String doEdit(@ModelAttribute Flight flight,
                         HttpSession session) throws SQLException {
        if (!checkRep(session)) return "redirect:/login";
        long mins = Duration.between(flight.getDepartTime(), flight.getArrivalTime()).toMinutes();
        flight.setAirtime((int)mins);
        flightDB.update(flight);
        return "redirect:/rep/flight/manage";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String flightNum,
                         HttpSession session) throws SQLException {
        if (!checkRep(session)) return "redirect:/login";
        flightDB.delete(flightNum);
        return "redirect:/rep/flight/manage";
    }

    @GetMapping("/by-airport")
    public String byAirport(
            @RequestParam(required = false) String airportId,
            HttpSession session, Model model
    ) throws SQLException {
        if (!checkRep(session)) return "redirect:/login";
        var airports = airportDB.findAll();
        if (airports.isEmpty()) {
            model.addAttribute("error", "No airports configured.");
            return "flightList";
        }
        if (airportId == null) {
            airportId = airports.get(0).getAirportId();
        }
        var departures = flightDB.findByOrigin(airportId);
        var arrivals   = flightDB.findByDestination(airportId);
        model.addAttribute("airports", airports);
        model.addAttribute("selectedAirport", airportId);
        model.addAttribute("departingFlights", departures);
        model.addAttribute("arrivingFlights",   arrivals);
        return "flightsByAirport";
    }
}

