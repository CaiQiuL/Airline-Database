package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.ReservationsConnect;
import com.Database.Airline.Objects.Ticket;
import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    @Autowired
    private ReservationsConnect reservationsConnect;

    private boolean checkAdmin(HttpSession session) {
        User me = (User) session.getAttribute("loggedInUser");
        return me != null && Boolean.TRUE.equals(me.getAdmin());
    }

    @GetMapping
    public String searchForm(
            @RequestParam(required = false) String flightNum,
            @RequestParam(required = false) String customerName,
            HttpSession session,
            Model model
    ) throws SQLException {
        if (!checkAdmin(session)) {
            model.addAttribute("error","Please log in as admin.");
            return "login";
        }

        model.addAttribute("flightNum", flightNum);
        model.addAttribute("customerName", customerName);

        if (flightNum != null && !flightNum.isBlank()) {
            List<Ticket> byFlight = reservationsConnect.findByFlightNum(flightNum);
            model.addAttribute("tickets", byFlight);
        } else if (customerName != null && !customerName.isBlank()) {
            List<Ticket> byCust = reservationsConnect.findByCustomerName(customerName);
            model.addAttribute("tickets", byCust);
        }

        return "reservationList";
    }
}
