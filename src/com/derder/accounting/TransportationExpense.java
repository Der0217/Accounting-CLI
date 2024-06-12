package com.derder.accounting;

public class TransportationExpense extends ExpenseEntry{
    public TransportationExpense(int year,int month,int day,String description, double amount) {
        super( year, month, day, description,amount);
    }
}
