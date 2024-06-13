package com.derder.accounting;

import com.derder.accounting.manager.ExpenseManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    static ExpenseManager contral = new ExpenseManager();
    static Scanner input = new Scanner(System.in);
    static enum Expense{
        food ,clothing ,education ,entertainment,housing ,other ,tax ,transportation
    }
    static{
        try {
            System.out.println("*系統* :載入先前檔案...");
            contral.loadToTxt();
            System.out.println("*系統* :載入完成!");
        } catch (FileNotFoundException e) {
            System.out.println("*警告* :載入先前檔案出現問題");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("*系統* :關閉程式中...");
            try {
                contral.writeToTxt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("*系統* :已成功保存數據並關閉程式。");
        }));
    }
    public static void main(String[] args) throws IOException {
        inputChose();
    }

    public static void inputChose() throws IOException {
        while(true){
            printMainMenu();
            String in = input.next();
            switch (in.toLowerCase()){
                case "a":
                    addExpense();
                    break;
                case "s":
                    contral.show();
                    break;
                case "q":
                    System.exit(0);
                    break;
                case "d":
                    deleteExpense();
                    break;
                case "r":
                    reSet();
                    break;
                default:
                    System.out.println("*警告* :無法辨別，請重新輸入");
            }
        }
    }
    public static void addExpense(){
        System.out.println("*系統* :請輸入欲新增的支出類型");
        String type = getExpenseType();
        if(type == null)
            return;
        
        System.out.println("*系統* :請輸入敘述");
        String description = input.next();
        
        System.out.println("*系統* :請輸入金額");
        double amount=getAmount();
        
        contral.addExpense(type,description,amount);
        System.out.println("*系統* :新增成功，將回到主選單\n");
    }
    public static void reSet(){
            while(true){
                System.out.println("*系統* :請輸入欲修改的支出類型");
                String type = getExpenseType();
                if(type==null)
                    return;
                if(contral.expenseIsEmpty(type)){
                    System.out.println("*警告* :該支出類型沒有資料\n");
                }

                System.out.println("*系統* :輸入欲修改支出資料編號");
                int idx = getIndex();

                if(!contral.checkRange(type, idx-1)) {
                    System.out.println("*警告* :該索引找不到項目");
                }else{
                    System.out.println("*系統* :請輸入敘述");
                    String description = input.next();

                    System.out.println("*系統* :請輸入金額");
                    double amount = getAmount();

                    contral.reSetExpense(type,idx-1,description,amount);
                    System.out.println("*系統* :修改完成! 返回主選單");
                    return;
                }
            }


    }
    public static void deleteExpense(){
        String type;

        contral.show();
        if(contral.expenseIsEmpty()){
            return;
        }
        while (true) {
            System.out.println("*系統* :請輸入欲刪除的支出類型");
            type = getExpenseType();
            if(type == null)
                return;
            if(contral.expenseIsEmpty(type) ){
                System.out.println("*警告* :該支出類型沒有資料\n");
                continue;
            }

            System.out.println("*系統* :輸入欲刪除編號");
            int idx = getIndex();

            if(!contral.checkRange(type, idx-1)) {
                System.out.println("*警告* :該索引找不到項目");
                continue;
            }else{
                contral.removeExpense(type, idx - 1);
                System.out.println("*系統* :刪除成功!，將回到主選單\n");
                return;
            }
        }
    }
    public static void printMainMenu(){
        System.out.println("--------------------*主選單*--------------------");
        System.out.println("*系統* :請輸入指令");
        System.out.println("|'a' or 'A' 新增支出項目| |'d' or 'D' 移除支出項目| ");
        System.out.println("|'s' or 'S' 顯示支出項目| |'r' or 'R' 修改支出項目|");
        System.out.println("|'q' or 'Q' 離開記帳程式|");
        System.out.println("-----------------------------------------------");
    }
    public static void printExpenseTypes(){
        System.out.println("1.food    2.clothing 3.education 4.entertainment ");
        System.out.println("5.housing 6.other    7.tax       8.transportation  9.返回到主選單");
    }

    public static String getExpenseType(){
        int num;
        printExpenseTypes();
        while(true){
            try{
                num = Integer.parseInt(input.next());
                if(num==9)
                    return null;
                if (num < 1 || num > 8) {
                    //丟出異常
                    throw new NumberFormatException();
                }
            }catch (NumberFormatException e){
                System.out.println("*警告* :輸入錯誤，請重新填寫\n");
                return null;
            }
            // enum.values(返回enum陣列)[](選擇index).name()->返回常量名稱
            return Expense.values()[num - 1].name();
        }
    }

    public static double getAmount(){
        double amount;
        while (true){
            try {
                amount = Double.valueOf(input.next());
                return amount;
            }catch (NumberFormatException e){
                System.out.println("*警告* :您輸入的不是整數，請重新輸入\n");
            }
        }
    }

    public static int getIndex(){
        int idx;
        while (true){
            try{
                idx = Integer.parseInt(input.next());
                return idx;
            }catch(NumberFormatException e){
                System.out.println("*警告* :您輸入的不是整數，請重新輸入\n");
            }
        }

    }
}
