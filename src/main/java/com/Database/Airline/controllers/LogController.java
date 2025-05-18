package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.UserAuth;
import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LogController {

    @Autowired
    private UserAuth userAuth;

    @GetMapping("/")
    public String LogPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        User user = userAuth.authenticate(username, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            model.addAttribute("message", "Login Successful");

            // **NEW**: branch on role
            if (user.getAdmin()) {
                return "adminPage";
            } else if (user.getRep()) {
                return "redirect:/rep/repPage";
            } else {
                return "homePage";
            }

        } else {
            model.addAttribute("error", "Login Failed");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate();
        model.addAttribute("message", "Logged out");
        return "login";
    }
}
