package com.Database.Airline.controllers;

import com.Database.Airline.DBConnection.QuestionDB;
import com.Database.Airline.Objects.Questions;
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
@RequestMapping("/rep/questions")
public class RepQuestionController {
    @Autowired
    private QuestionDB questionsDB;

    private boolean checkRep(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && Boolean.TRUE.equals(u.getRep());
    }

    @GetMapping
    public String list(HttpSession session, Model model) throws SQLException {
        if (!checkRep(session)) return "redirect:/login";
        List<Questions> qs = questionsDB.findAll();
        model.addAttribute("questions", qs);
        return "questionsList";
    }

    @GetMapping("/reply")
    public String form(
            @RequestParam int messageID,
            HttpSession session,
            Model model
    ) throws SQLException {
        if (!checkRep(session)) return "redirect:/login";
        Questions q = questionsDB.findById(messageID);
        if (q == null) throw new IllegalArgumentException("No such question: "+messageID);
        model.addAttribute("question", q);
        return "questionsForm";
    }

    @PostMapping("/reply")
    public String doReply(
            @RequestParam int messageID,
            @RequestParam String answer,
            HttpSession session
    ) throws SQLException {
        if (!checkRep(session)) return "redirect:/login";
        User rep = (User) session.getAttribute("loggedInUser");
        questionsDB.updateAnswer(messageID, answer, rep.getUserID());
        return "redirect:/rep/questions";
    }
}
