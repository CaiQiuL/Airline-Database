package com.Database.Airline.Objects;

import java.time.LocalDateTime;

public class Ticket {

    private int ticket_num;
    private int userID;
    private String flight_num;
    private int seat_number;
    private String Uclass;
    private float bookingFee;
    private float total;
    private LocalDateTime purchase_time;
    private String goingTo;
    private String comingFrom;

    public Ticket(int ticket_num, int userID, String flight_num, int seat_number, String uclass, float bookingFee, float total, LocalDateTime purchase_time, String goingTo, String comingFrom) {
        this.ticket_num = ticket_num;
        this.userID = userID;
        this.flight_num = flight_num;
        this.seat_number = seat_number;
        Uclass = uclass;
        this.bookingFee = bookingFee;
        this.total = total;
        this.purchase_time = purchase_time;
        this.goingTo = goingTo;
        this.comingFrom = comingFrom;
    }

    public int getTicket_num() {
        return ticket_num;
    }

    public void setTicket_num(int ticket_num) {
        this.ticket_num = ticket_num;
    }

    public String getFlight_num() {
        return flight_num;
    }

    public void setFlight_num(String flight_num) {
        this.flight_num = flight_num;
    }

    public String getUclass() {
        return Uclass;
    }

    public void setUclass(String uclass) {
        Uclass = uclass;
    }

    public int getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(int seat_number) {
        this.seat_number = seat_number;
    }

    public float getBooking_fee() {
        return bookingFee;
    }

    public void setBooking_fee(float booking_fee) {
        this.bookingFee = booking_fee;
    }

    public float getTotal_fare() {
        return total;
    }

    public void setTotal_fare(float total_fare) {
        this.total = total_fare;
    }

    public LocalDateTime getPurchase_time() {
        return purchase_time;
    }

    public void setPurchase_time(LocalDateTime purchase_time) {
        this.purchase_time = purchase_time;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getBookingFee() {
        return bookingFee;
    }

    public void setBookingFee(float bookingFee) {
        this.bookingFee = bookingFee;
    }

    public String getGoingTo() {
        return goingTo;
    }

    public void setGoingTo(String goingTo) {
        this.goingTo = goingTo;
    }

    public String getComingFrom() {
        return comingFrom;
    }

    public void setComingFrom(String comingFrom) {
        this.comingFrom = comingFrom;
    }
}
