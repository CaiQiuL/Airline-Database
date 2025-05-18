package com.Database.Airline.controllers;


import com.Database.Airline.DBConnection.ReportDB;
import com.Database.Airline.DBConnection.UserDB;
import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminController {
    @Autowired
    private UserDB userDB;
    @Autowired
    private ReportDB reportDB;

    private boolean checkAdmin(HttpSession session) {
        User me = (User)session.getAttribute("loggedInUser");
        return me != null && Boolean.TRUE.equals(me.getAdmin());
    }

    @GetMapping
    public String list(HttpSession session, Model model) throws SQLException {
        if (!checkAdmin(session)) return "redirect:/login";
        List<User> all = userDB.findAll();
        model.addAttribute("users", all);
        return "userList";
    }

    @GetMapping("/create")
    public String createForm(HttpSession session, Model model) {
        if (!checkAdmin(session)) return "redirect:/login";
        model.addAttribute("user", new User());
        return "userForm";
    }

    @PostMapping("/create")
    public String doCreate(
            @ModelAttribute User user,
            HttpSession session
    ) throws SQLException {
        if (!checkAdmin(session)) return "redirect:/login";
        userDB.create(user, false, false);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit")
    public String editForm(
            @RequestParam int userID,
            HttpSession session,
            Model model
    ) throws SQLException {
        if (!checkAdmin(session)) return "redirect:/login";
        User u = userDB.findById(userID);
        if (u == null) throw new IllegalArgumentException("No such user");
        model.addAttribute("user", u);
        return "userForm";
    }

    @PostMapping("/edit")
    public String doEdit(
            @ModelAttribute User user,
            @RequestParam(name="rep",   defaultValue="false") boolean isRep,
            @RequestParam(name="admin", defaultValue="false") boolean isAdmin,
            HttpSession session
    ) throws SQLException {
        if (!checkAdmin(session)) return "redirect:/login";
        userDB.update(user, isRep, isAdmin);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete")
    public String delete(
            @RequestParam int userID,
            HttpSession session
    ) throws SQLException {
        if (!checkAdmin(session)) return "redirect:/login";
        userDB.delete(userID);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin")
    public String adminHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !Boolean.TRUE.equals(user.getAdmin())) {
            model.addAttribute("error", "Please log in as an admin.");
            return "login";
        }
        model.addAttribute("adminName", user.getFname());
        return "adminPage";
    }

    @GetMapping("/active-flights")
    public String activeFlights(
            @RequestParam(defaultValue="10") int topN,
            HttpSession session,
            Model model
    ) throws SQLException {
        if (!checkAdmin(session)) return "redirect:/login";
        var list = reportDB.findMostActiveFlights(topN);
        model.addAttribute("flights", list);
        model.addAttribute("topN", topN);
        return "activeFlights";
    }
}
