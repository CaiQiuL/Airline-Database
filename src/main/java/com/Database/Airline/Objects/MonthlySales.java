package com.Database.Airline.Objects;

public class MonthlySales {
    private int year;
    private int month;
    private float Sales;

    public MonthlySales(int year, int month, float sales) {
        this.year = year;
        this.month = month;
        Sales = sales;
    }

    public float getSales() {
        return Sales;
    }

    public void setSales(float sales) {
        Sales = sales;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
