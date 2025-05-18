package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.QuestionDB;
import com.Database.Airline.DBConnection.ReservationsConnect;
import com.Database.Airline.DBConnection.TicketCreate;
import com.Database.Airline.Objects.Ticket;
import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/rep")
public class RepController {
    @Autowired
    private TicketCreate ticketCreate;
    @Autowired
    private ReservationsConnect reservationsConnect;
    @Autowired
    private QuestionDB questionDB;

    private boolean checkRep(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && Boolean.TRUE.equals(u.getRep());
    }

    @GetMapping("/repPage")
    public String repHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !user.getRep()) {
            model.addAttribute("error","Please log in as a rep.");
            return "login";
        }
        model.addAttribute("repName", user.getFname());
        return "repPage";
    }
    @GetMapping("/book-for")
    public String showBookForForm() {
        return "bookForForm";
    }

    @PostMapping("/book-for")
    public String bookFor(
            @RequestParam int customerId,
            @RequestParam String flightType,
            @RequestParam String outboundFlightNum,
            @RequestParam(required = false) String returnFlightNum,
            @RequestParam String flightClass,
            Model model
    ) throws SQLException {
        List<Ticket> tickets = ticketCreate.bookFlights(
                customerId, flightType, outboundFlightNum, returnFlightNum, flightClass
        );
        model.addAttribute("tickets", tickets);
        return "bookForResult";
    }
    @GetMapping("/edit-reservation")
    public String editReservationPage(
            @RequestParam(required=false) Integer ticketNum,
            HttpSession session,
            Model model
    ) throws SQLException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !user.getRep()) {
            model.addAttribute("error","Please log in as a rep.");
            return "login";
        }

        if (ticketNum == null) {
            var all = reservationsConnect.findAllTickets();
            model.addAttribute("tickets", all);
            return "editReservationSearch";
        }
        Ticket t = reservationsConnect.findByTicketNum(ticketNum);
        if (t == null) throw new IllegalArgumentException("No such ticket: "+ticketNum);
        model.addAttribute("ticket", t);
        return "editReservationForm";
    }
    @PostMapping("/edit-reservation")
    public String doEditReservation(
            @RequestParam int ticketNum,
            @RequestParam String newFlightNum,
            @RequestParam String newClass,
            HttpSession session,
            Model model
    ) throws SQLException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !user.getRep()) {
            model.addAttribute("error","Please log in as a rep.");
            return "login";
        }
        boolean ok = reservationsConnect.updateReservation(ticketNum, newFlightNum, newClass);
        model.addAttribute("message", ok ? "Reservation updated." : "Update failed.");
        return "editReservationResult";
    }
    @GetMapping("/waitlist")
    public String viewWaitlist(
            @RequestParam(required = false) String flightNum,
            HttpSession session,
            Model model
    ) throws SQLException {
        if (!checkRep(session)) {
            return "redirect:/login";
        }
        model.addAttribute("flightNum", flightNum);
        if (flightNum != null && !flightNum.isBlank()) {
            var entries = reservationsConnect.findWaitlistByFlight(flightNum);
            model.addAttribute("entries", entries);
        }
        return "waitlist";
    }
    @GetMapping("/rep/questions")
    public String listQuestions(HttpSession session, Model model) throws SQLException {
        model.addAttribute("questions", questionDB.findAll());
        return "rep/questionsList";
    }

    @GetMapping("/rep/questions/{id}")
    public String showAnswerForm(@PathVariable int id, Model model) throws SQLException {
        model.addAttribute("q", questionDB.findById(id));
        return "rep/questionsForm";
    }

    @PostMapping("/rep/questions/{id}")
    public String submitAnswer(@PathVariable int id,
                               @RequestParam String answer,
                               HttpSession session) throws SQLException {
        int repId = ((User)session.getAttribute("loggedInUser")).getUserID();
        questionDB.updateAnswer(id, answer, repId);
        return "redirect:/rep/questions";
    }
}
