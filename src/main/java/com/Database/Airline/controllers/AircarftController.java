package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.AircraftDB;
import com.Database.Airline.Objects.Aircraft;
import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rep/aircraft")
public class AircarftController {
    @Autowired
    private AircraftDB aircraftDB;

    private boolean checkRep(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && Boolean.TRUE.equals(u.getRep());
    }

    @GetMapping("/manage")
    public String list(HttpSession session, Model model) throws Exception {
        if (!checkRep(session)) {
            return "redirect:/login";
        }
        model.addAttribute("aircrafts", aircraftDB.findAll());
        return "aircraftList";
    }

    @GetMapping("/create")
    public String createForm(HttpSession session, Model model) {
        if (!checkRep(session)) {
            return "redirect:/login";
        }
        model.addAttribute("aircraft", new Aircraft());
        return "aircraftForm";
    }

    @PostMapping("/create")
    public String doCreate(@ModelAttribute Aircraft aircraft,
                           HttpSession session) throws Exception {
        if (!checkRep(session)) {
            return "redirect:/login";
        }
        aircraftDB.create(aircraft);
        return "redirect:/rep/aircraft/manage";
    }

    @GetMapping("/edit")
    public String editForm(@RequestParam String craftNum,
                           HttpSession session,
                           Model model) throws Exception {
        if (!checkRep(session)) {
            return "redirect:/login";
        }
        Aircraft a = aircraftDB.findById(craftNum);
        if (a == null) {
            throw new IllegalArgumentException("No such aircraft: " + craftNum);
        }
        model.addAttribute("aircraft", a);
        // was "rep/aircraftForm"
        return "aircraftForm";
    }

    @PostMapping("/edit")
    public String doEdit(@ModelAttribute Aircraft aircraft,
                         HttpSession session) throws Exception {
        if (!checkRep(session)) {
            return "redirect:/login";
        }
        aircraftDB.update(aircraft);
        return "redirect:/rep/aircraft/manage";
    }

    /** Delete an aircraft */
    @GetMapping("/delete")
    public String delete(@RequestParam String craftNum,
                         HttpSession session) throws Exception {
        if (!checkRep(session)) {
            return "redirect:/login";
        }
        aircraftDB.delete(craftNum);
        return "redirect:/rep/aircraft/manage";
    }
}
