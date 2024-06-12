package com.derder.accounting;

import java.text.MessageFormat;
import java.time.LocalDate;

public class ExpenseEntry {
    //Time set
    private int year;
    private int month;
    private int day;

    //Set description and amount
    private String description;
    private double amount;


    //struct
    public ExpenseEntry(int year,int month,int day,String description, double amount) {
        this.description = description;
        this.amount = amount;
        this.year=year;
        this.month = month;
        this.day = day;
    }
    public int getYear() {
        return year;
    }

    public int getMonuth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getDescription() {return description;}

    public double getAmount() {
        return amount;
    }
    public String showDate(){
        String formattedString = String.format("時間: %d-%02d-%02d, 敘述: %s, 金額: %.2f",
                 year, month, day, description, amount);
        return formattedString;
    }
    public void setDescriptionAndAmount(String description,double amount){
        this.description=description;
        this.amount=amount;
    }
}
