package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.AirportDB;
import com.Database.Airline.Objects.Airport;
import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rep/airport")
public class AirportController {
    @Autowired
    private AirportDB airportDB;

    private boolean isRep(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && Boolean.TRUE.equals(u.getRep());
    }

    @GetMapping("/manage")
    public String list(HttpSession session, Model model) throws Exception {
        if (!isRep(session)) return "redirect:/login";
        model.addAttribute("airports", airportDB.findAll());
        return "airportList";
    }

    @GetMapping("/create")
    public String createForm(HttpSession session, Model model) {
        if (!isRep(session)) return "redirect:/login";
        model.addAttribute("airport", new Airport());
        return "airportForm";
    }

    @PostMapping("/create")
    public String doCreate(@ModelAttribute Airport airport, HttpSession session) throws Exception {
        if (!isRep(session)) return "redirect:/login";
        airportDB.create(airport);
        return "redirect:/rep/airport/manage";
    }

    @GetMapping("/edit")
    public String editForm(
            @RequestParam String airportId,
            HttpSession session,
            Model model
    ) throws Exception {
        if (!isRep(session)) return "redirect:/login";
        Airport a = airportDB.findById(airportId);
        if (a == null) throw new IllegalArgumentException("No such airport: " + airportId);
        model.addAttribute("airport", a);
        return "airportForm";
    }

    @PostMapping("/edit")
    public String doEdit(@ModelAttribute Airport airport, HttpSession session) throws Exception {
        if (!isRep(session)) return "redirect:/login";
        airportDB.update(airport);
        return "redirect:/rep/airport/manage";
    }

    @GetMapping("/delete")
    public String delete(
            @RequestParam String airportId,
            HttpSession session
    ) throws Exception {
        if (!isRep(session)) return "redirect:/login";
        airportDB.delete(airportId);
        return "redirect:/rep/airport/manage";
    }
}
