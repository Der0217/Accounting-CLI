package com.derder.accounting.type;

public class TaxExpense extends ExpenseEntry{
    public TaxExpense(int year,int month,int day,String description, double amount) {
        super( year, month, day, description,  amount);
    }
}
