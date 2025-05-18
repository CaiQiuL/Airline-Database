package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.ReservationsConnect;
import com.Database.Airline.Objects.Ticket;
import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationsController {
    @Autowired
    private ReservationsConnect reservationsConnect;

    @GetMapping()
    public String myReservations(HttpSession session, Model model) throws SQLException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<Ticket> tickets = reservationsConnect.findByUser(user.getUserID());
        model.addAttribute("tickets", tickets);
        return "reservations";
    }

    public String showReservations(HttpSession session, Model model) throws SQLException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error", "Please log in first.");
            return "login";
        }
        List<Ticket> tickets = reservationsConnect.findByUser(user.getUserID());
        model.addAttribute("tickets", tickets);
        return "reservations";
    }

    @PostMapping("/cancel")
    public String cancel(
            @RequestParam int ticketNum,
            HttpSession session,
            Model model
    ) throws SQLException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error", "Please log in first.");
            return "login";
        }
        List<Ticket> mine = reservationsConnect.findByUser(user.getUserID());
        Ticket toCancel = mine.stream()
                .filter(t -> t.getTicket_num() == ticketNum)
                .findFirst()
                .orElse(null);
        if (toCancel == null) {
            model.addAttribute("error", "Ticket not found.");
        } else if (!("Business".equalsIgnoreCase(toCancel.getUclass())
                || "First".equalsIgnoreCase(toCancel.getUclass()))) {
            model.addAttribute("error",
                    "Only Business or First class tickets may be cancelled.");
        } else {
            boolean ok = reservationsConnect.cancelTicket(ticketNum);
            model.addAttribute("message",
                    ok ? "Ticket cancelled."
                            : "Failed to cancel ticket.");
        }
        model.addAttribute("tickets",
                reservationsConnect.findByUser(user.getUserID()));
        return "reservations";
    }
}
