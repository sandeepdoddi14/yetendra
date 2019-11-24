package com.darwinbox.reimbursement.objects.ReimbCreation;

import com.darwinbox.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Expenses extends Services {
    //Form Type to be created on runtime? in Services class.
    ReimbForm reimbForm = new ReimbForm();
    private String userId;
    private String expenseId;
    private String title;
    private String date;

    private String currency;//this should be of reimbForm type
    private String NoOfunits;
    private String amount;
    private String description;
    private ReimbForm formType;

    public String getUserId() {  return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getExpenseId() {  return expenseId;  }

    public void setExpenseId(String expenseId) {  this.expenseId = expenseId; }

    public String getTitle() {  return title;  }

    public void setTitle(String title) {  this.title = title; }

    public String getDate() { return String.valueOf(date); }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNoOfunits() {
        return NoOfunits;
    }

    public void setNoOfunits(String noOfunits) {
        NoOfunits = noOfunits;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ReimbForm getFormType() {
        return formType;
    }

    public void setFormType(ReimbForm formType) {
        this.formType = formType;
    }

    public void toObject(Map<String, String> data) {
        setTitle(data.get("Title"));
        setDate(data.get("Date"));
        setCurrency(data.get("Currency"));
        setNoOfunits(data.get("Units"));
        setAmount(data.get("Amount"));
        setDescription(data.get("Description"));
    }

    public List<NameValuePair> toMap() {
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("UserExpensesForm[user_id]", "5a841e4e54b97"));
        body.add(new BasicNameValuePair("UserExpensesForm[expense_id]", getExpenseId()));
        body.add(new BasicNameValuePair("UserExpensesForm[type]", getFormType().getId()));
        body.add(new BasicNameValuePair("label_text",""));
        body.add(new BasicNameValuePair("unit_name",""));
        body.add(new BasicNameValuePair("auto_cal","1"));
        body.add(new BasicNameValuePair("rupee","0"));
        //fields mapped with formTYpe Id: label_text, unit_name, auto_cal, rupee(currency chosen)
        body.add(new BasicNameValuePair("UserExpensesForm[title]", getTitle()));
        body.add(new BasicNameValuePair("UserExpensesForm[date]", getDate()));
        body.add(new BasicNameValuePair("UserExpensesForm[currency]", getCurrency().toUpperCase()));
        body.add(new BasicNameValuePair("UserExpensesForm[amount]", getAmount() + ""));
        //amount is passed directly only when units are not assigned to the form type selected, else amt per unit has to be given.
        body.add(new BasicNameValuePair("UserExpensesForm[unit]", getNoOfunits() + ""));
        body.add(new BasicNameValuePair("UserExpensesForm[itemName]", getDescription()));
        body.add(new BasicNameValuePair("upload[]", ""));
        //body.add(new BasicNameValuePair("shouldUpload[]","[]"));
        return body;
    }
}

