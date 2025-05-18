package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.ReportDB;
import com.Database.Airline.Objects.CustomerRevenue;
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

@Controller
@RequestMapping("/admin/reports")
public class AdminReportsController {

    @Autowired private ReportDB reportDB;

    private boolean checkAdmin(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && Boolean.TRUE.equals(u.getAdmin());
    }

    @GetMapping("/revenue")
    public String form(HttpSession session) {
        if (!checkAdmin(session)) return "redirect:/login";
        return "revenueForm";
    }

    @PostMapping("/revenue")
    public String result(
            @RequestParam String reportType,
            @RequestParam String identifier,
            HttpSession session,
            Model model
    ) throws SQLException {
        if (!checkAdmin(session)) return "redirect:/login";
        double revenue;
        switch (reportType) {
            case "flight":
                revenue = reportDB.revenueByFlight(identifier);
                break;
            case "airline":
                revenue = reportDB.revenueByAirline(identifier);
                break;
            case "customer":
                revenue = reportDB.revenueByCustomer(Integer.parseInt(identifier));
                break;
            default:
                revenue = 0.0;
        }
        model.addAttribute("reportType", reportType);
        model.addAttribute("identifier", identifier);
        model.addAttribute("revenue", revenue);
        return "revenueResult";
    }

    @GetMapping("/top-customer")
    public String topCustomer(HttpSession session, Model model) throws SQLException {
        if (!checkAdmin(session)) return "redirect:/login";
        CustomerRevenue cr = reportDB.findTopCustomer();
        model.addAttribute("customer", cr);
        return "topCustomer";
    }
}
