package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.SalesDB;
import com.Database.Airline.Objects.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;


@Controller
public class AdminReportController {
    @Autowired
    private SalesDB salesDB;

    private boolean checkAdmin(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && Boolean.TRUE.equals(u.getAdmin());
    }

    @GetMapping("/admin/reports/sales/monthly")
    public String monthlySales(HttpSession session, Model model) throws SQLException {
        User u = (User)session.getAttribute("loggedInUser");
        if (u == null || !u.getAdmin()) {
            model.addAttribute("error","Please log in as an admin.");
            return "login";
        }
        model.addAttribute("report", salesDB.getMonthlySales());
        return "monthlySalesReport";
    }
}
