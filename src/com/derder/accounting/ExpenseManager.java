package com.derder.accounting;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class ExpenseManager {
    private static Map<String,List<ExpenseEntry>> allExpense= new HashMap();
    private LocalDate date = LocalDate.now();
    String dataPath = "src/Data.txt";
    static {
        // 初始化所有支出類型列表
        allExpense.put("food", new ArrayList<>());
        allExpense.put("clothing", new ArrayList<>());
        allExpense.put("education", new ArrayList<>());
        allExpense.put("entertainment", new ArrayList<>());
        allExpense.put("housing", new ArrayList<>());
        allExpense.put("other", new ArrayList<>());
        allExpense.put("tax", new ArrayList<>());
        allExpense.put("transportation", new ArrayList<>());
    }
    // 載入檔案所有支出資料
    public  void loadToTxt() throws IOException {
        File file = new File(dataPath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                String []spiltPart=line.split("\\\\");
                String type =spiltPart[0];
                int year = Integer.parseInt(spiltPart[1]);
                int month = Integer.parseInt(spiltPart[2]);
                int day = Integer.parseInt(spiltPart[3]);
                String description = spiltPart[4];
                double amount = Double.valueOf(spiltPart[5]);
                addExpense(type,year,month,day,description,amount);
            }
        }catch (IOException w){
            w.printStackTrace();
        }
        sortExpenseDate(); // 載入後進行排序
    }

    // 將支出資料寫入txt檔
    public void writeToTxt() throws IOException {
        File file = new File(dataPath);
        FileWriter fileWriter = new FileWriter(file);
        allExpense.forEach((category, expenseEntries)-> {
            StringBuilder sb = new StringBuilder();
            if(!expenseEntries.isEmpty()){
                expenseEntries.forEach(expenseEntry->{
                        sb.append(category+"\\"+expenseEntry.getYear()+"\\"+expenseEntry.getMonuth()+
                                "\\"+expenseEntry.getDay()+"\\"+expenseEntry.getDescription()+"\\"+expenseEntry.getAmount()+"\n");
                });
                try {
                    fileWriter.write(sb.toString());
                    fileWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        fileWriter.close();
    }
    // 添加當天日期的支出資料
    public void addExpense(String type,String description, double amount){
        LocalDate date = LocalDate.now();
        ExpenseEntry expense = ExpenseEntryFactory.createExpenseEntry(
                type, date.getYear(), date.getMonthValue(), date.getDayOfMonth(), description, amount);
        allExpense.get(type).add(expense);
        sortExpenseDate();
    }
    // Overidding 添加指定日期的支出資料
    public void addExpense(String type,int year,int month,int day,String description, double amount){
        ExpenseEntry expense = ExpenseEntryFactory.createExpenseEntry(
                type, year, month, day, description, amount);
        allExpense.get(type).add(expense);
        sortExpenseDate();
    }
    // 移除指定的支出資料
    public void removeExpense(String type, int index) {
        allExpense.get(type).remove(index);
    }

    //輸出所有支出資料、還有類型
    public  void show() {
        if(expenseIsEmpty()){
            System.out.println("*系統* :目前沒有資料");
            return;
        }
        System.out.println("--------------------*記帳清單*--------------------");
        allExpense.forEach((category, expenseEntries) -> {
            if (!expenseEntries.isEmpty()) {
                System.out.println("類型{" + category + "}");
                // AtomicInteger 原子性 可避免多線程產生競爭狀況
                final AtomicInteger index = new AtomicInteger(1);
                expenseEntries.forEach(expenseEntry -> {
                    System.out.println(index.getAndIncrement() + " " + expenseEntry.showDate());
                });

            }
        });
        System.out.println("------------------------------------------------");
    }
    //檢查全部是否為空
    public boolean expenseIsEmpty() {
        //allMatch(List::isEmpty) 檢查該map所有是否符合()內判斷
        return allExpense.values().stream().allMatch(List::isEmpty);
    }
    // Overidding 檢查特定類型是否為空
    public boolean expenseIsEmpty(String type) {
        //getOrDefault(key,defaultValue) Map有該key則return 該映射值，否則回傳defaultValue *Collections.emptyList()是一個空集合
        return allExpense.getOrDefault(type, Collections.emptyList()).isEmpty();
    }
    // 檢查index是否在範圍內
    public boolean checkRange(String type, int index) {
        return allExpense.containsKey(type) && index >= 0 && index < allExpense.get(type).size();
    }
    // 依照日期對支出資料進行排序
    public void sortExpenseDate() {
        allExpense.forEach((category, expenseEntries) -> {
            if (!expenseEntries.isEmpty()) {
                //Comparator.comparing 對每一值進行比較，thanComparing是前面比較完跟著比較，先比較年再來月最後是日
                expenseEntries.sort(Comparator.comparing(ExpenseEntry::getYear)
                        .thenComparing(ExpenseEntry::getMonuth).thenComparing(ExpenseEntry::getDay));
            }
        });
    }
    // 對該支出資料進行設定
    public void reSetExpense(String type , int index , String description,double amount){
        allExpense.get(type).get(index).setDescriptionAndAmount(description,amount);
    }
}
