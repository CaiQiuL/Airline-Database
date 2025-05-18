package com.Database.Airline.controllers;

import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {
    @GetMapping("/admin")
    public String adminHome(HttpSession session, Model model) {
        User user = (User)session.getAttribute("loggedInUser");
        if (user == null || !Boolean.TRUE.equals(user.getAdmin())) {
            model.addAttribute("error","Please log in as an admin.");
            return "login";
        }
        model.addAttribute("adminName", user.getFname());
        return "adminPage";
    }
}
