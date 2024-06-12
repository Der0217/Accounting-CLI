package com.derder.accounting;

import java.time.LocalDate;

public class ExpenseEntryFactory {
    public static ExpenseEntry createExpenseEntry(String type, int year, int month, int day, String description, double amount) {
        switch (type) {
            case "food":
                return new FoodExpense(year, month, day, description, amount);
            case "clothing":
                return new ClothingExpense(year, month, day, description, amount);
            case "education":
                return new EducationExpense(year, month, day, description, amount);
            case "entertainment":
                return new EntertainmentExpense(year, month, day, description, amount);
            case "housing":
                return new HousingExpense(year, month, day, description, amount);
            case "other":
                return new OtherExpense(year, month, day, description, amount);
            case "tax":
                return new TaxExpense(year, month, day, description, amount);
            case "transportation":
                return new TransportationExpense(year, month, day, description, amount);
            default:
                throw new IllegalArgumentException("Unknown expense type: " + type);
        }
    }
}
