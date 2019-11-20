package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Expenses extends Services {
//Form Type to be created on runtime? in Services class?
    private String userId;
    private String expenseId;
    private String title;
    private String type;
    private String date;
    private String NoOfunits;
    private String currency;//this should be of reimbForm type
    private String description;
    ReimbForm reimbForm = new ReimbForm();
    private String formType;
    private int amount = 0;

    public String getUserId() { return userId; }

    public void setUserId(String userId) {  this.userId = userId;  }

    public String getExpenseId() {   return expenseId; }

    public void setExpenseId(String expenseId) { this.expenseId = expenseId; }

    public String getFormType() {  return formType;  }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getTitle() {  return title; }

    public void setTitle(String title) {   this.title = title;  }

    public String getType() {  return type; }

    public void setType(String type) { this.type = type;  }

    public String getDate() {   return String.valueOf(date); }

    public void setDate(String date) {   this.date = date;  }

    public String getNoOfunits() {   return NoOfunits; }

    public void setNoOfunits(String noOfunits) { NoOfunits = noOfunits; }

    public String getCurrency() {    return currency;  }

    public void setCurrency(String currency) { this.currency = currency;   }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {  return amount;   }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void toObject(Map<String, String> data) {
        setTitle(data.get("Title"));
        setDate(data.get("Date"));
        setCurrency(data.get("Currency"));
        setNoOfunits(data.get("Units"));
        setAmount(Integer.parseInt(data.get("Amount")));
        setDescription(data.get("Description"));
        setFormType(data.get("FormType"));
    }

    public List<NameValuePair> toMap() {
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("UserExpensesForm[user_id]" , "5a841e4e54b97"));
        body.add(new BasicNameValuePair("UserExpensesForm[expense_id]", getExpenseId()));
        body.add(new BasicNameValuePair("UserExpensesForm[type]" , getFormType()));
        //getFormType().getName())
        //fields mapped with formTYpe Id: label_text, unit_name, auto_cal, rupee(currency chosen)
        body.add(new BasicNameValuePair("UserExpensesForm[title]", getTitle()));
        body.add(new BasicNameValuePair("UserExpensesForm[date]", getDate()));
        body.add(new BasicNameValuePair("UserExpensesForm[currency]", getCurrency().toString().toUpperCase()));
        body.add(new BasicNameValuePair("UserExpensesForm[amount]", getAmount() + ""));
        body.add(new BasicNameValuePair("UserExpensesForm[unit]", getNoOfunits()));
        body.add(new BasicNameValuePair("UserExpensesForm[itemName]" ,getDescription()));
        body.add(new BasicNameValuePair("upload[]",""));
        body.add(new BasicNameValuePair("shouldUpload[]","[]"));
        return body;
    }
}

