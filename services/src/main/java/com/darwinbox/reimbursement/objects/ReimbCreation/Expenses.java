package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.core.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Expenses extends Services {

    private String userId;
    private String expenseId;
    private String title;
    private String type;
    private String date;
    private String currency;
    private int amount = 0;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return String.valueOf(date);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void toObject(Map<String, String> data) {
        setTitle(data.get("Title"));
        setType(data.get("Type"));
        setDate(data.get("Date"));
        setCurrency(data.get("Currency"));
        setAmount(Integer.parseInt(data.get("Amount")));
    }

    public List<NameValuePair> toMap() {
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("UserExpensesForm[title]", getTitle()));
        body.add(new BasicNameValuePair("UserExpensesForm[type]", getType()));
        body.add(new BasicNameValuePair("UserExpensesForm[date]", getDate()));
        body.add(new BasicNameValuePair("UserExpensesForm[currency]", getCurrency()));
        body.add(new BasicNameValuePair("UserExpensesForm[amount]", getAmount() + ""));
//        UserExpensesForm[itemName]
//        UserExpensesForm[unit]

        return body;
    }
}

