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
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionDB questionDB;

    @GetMapping
    public String showAllQuestions(
            @RequestParam(required = false) Integer messageID,
            HttpSession session,
            Model model
    ) throws SQLException {
        if (session.getAttribute("loggedInUser") == null) {
            model.addAttribute("error","Please log in first.");
            return "login";
        }

        List<Questions> list;
        if (messageID != null) {
            // single-item list
            Questions q = questionDB.findByMessageID(messageID);
            list = List.of(q);
        } else {
            list = questionDB.findAllQuestions();
        }
        model.addAttribute("questions", list);
        model.addAttribute("filterId", messageID);
        return "questions";
    }

    @PostMapping
    public String postQuestion(
            @RequestParam String questionText,
            HttpSession session
    ) throws SQLException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        questionDB.postQuestion(user.getUserID(), questionText);
        return "redirect:/questions";
    }

    @GetMapping("/rep")
    public String showUnanswered(HttpSession session, Model model) throws SQLException {
        User rep = (User) session.getAttribute("loggedInUser");
        if (rep == null) {
            return "redirect:/login";
        }
        List<Questions> list = questionDB.findUnanswered();
        model.addAttribute("questions", list);
        return "questionsRep";
    }

    @PostMapping("/answer")
    public String answerQuestion(
            @RequestParam int messageID,
            @RequestParam String answerText,
            HttpSession session
    ) throws SQLException {
        User rep = (User) session.getAttribute("loggedInUser");
        if (rep == null) {
            return "redirect:/login";
        }
        questionDB.answerQuestion(messageID, rep.getUserID(), answerText);
        return "redirect:/questions/rep";
    }
}
