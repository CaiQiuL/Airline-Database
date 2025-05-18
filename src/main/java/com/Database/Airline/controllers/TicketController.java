package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.ReservationsConnect;
import com.Database.Airline.DBConnection.TicketCreate;
import com.Database.Airline.Objects.Ticket;
import com.Database.Airline.Objects.User;
import com.Database.Airline.Objects.Waitlist;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketCreate ticketCreate;
    @Autowired
    private ReservationsConnect reservationsConnect;

    @PostMapping("/book")
    public String bookFlights(
            @RequestParam String flightType,
            @RequestParam String flightClass,
            @RequestParam String outboundFlightNum,
            @RequestParam(required = false) String returnFlightNum,
            HttpSession session,
            Model model
    ) throws SQLException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error","Please log in first.");
            return "login";
        }
        List<Ticket> tickets = ticketCreate.bookFlights(
                user.getUserID(),
                flightType,
                outboundFlightNum,
                returnFlightNum,
                flightClass
        );
        model.addAttribute("outTicket", tickets.get(0));
        if (tickets.size() > 1) {
            model.addAttribute("returnTicket", tickets.get(1));
        }
        return "bookingConfirmation";
    }

    @GetMapping("/waitlist")
    public String myWaitlist(HttpSession session, Model model) throws SQLException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error","Please log in first.");
            return "login";
        }
        List<Waitlist> waitingList = reservationsConnect.findWaitlistByUser(user.getUserID());
        model.addAttribute("waitingList", waitingList);
        return "waitingList";
    }
}