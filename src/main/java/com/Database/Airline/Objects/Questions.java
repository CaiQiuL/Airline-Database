package com.Database.Airline.Objects;

import java.time.LocalDateTime;

public class Questions {
    private int messageID;
    private int customerID;
    private int repID;
    private String question;
    private String answer;
    private LocalDateTime timestamp;

    public Questions(int messageID, int customerID, int repID, String answer, String question, LocalDateTime timestamp) {
        this.messageID = messageID;
        this.customerID = customerID;
        this.repID = repID;
        this.answer = answer;
        this.question = question;
        this.timestamp = timestamp;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getRepID() {
        return repID;
    }

    public void setRepID(int repID) {
        this.repID = repID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
