package com.darwinbox.reimbursement.services;

import com.darwinbox.Services;
import com.darwinbox.reimbursement.objects.ReimbCreation.Expenses;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReimbExpenseService extends Services {
//If auto-approved/Rejected/Approved/Processed, Expense can't be edited/revoked anymore : How to check this?

    public String validateExpenseAsEmployee(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/validate");
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> obj = expenses.toMap();
        obj.add(new BasicNameValuePair("shouldUpload[]","[]"));

        String response = doPost(url, headers, obj);
        return response;
    }

    public String createExpenseAsEmployee(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/applyreimbursement");
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> obj = expenses.toMap();
        obj.add(new BasicNameValuePair("yt1", "Submit"));

        String response = doPost(url, headers, obj);
        return response;
    }

    public void editExpenseAsEmployee(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/applyreimbursement");
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        List<NameValuePair> obj = expenses.toMap();
        obj.add(new BasicNameValuePair("UserExpensesForm[user_id]", expenses.getUserId()));
        obj.add(new BasicNameValuePair("UserExpensesForm[expense_id]", expenses.getExpenseId()));

        obj.add(new BasicNameValuePair("yt0", "Submit"));
        String response = doPost(url, headers, obj);
        waitForUpdate(3);
    }

    public String revokeExpenseAsEmployee(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/DetailedView");
        Map headers = new HashMap();

        List<NameValuePair> obj = new ArrayList<>();
        obj.add(new BasicNameValuePair("id", expenses.getExpenseId()));
        obj.add(new BasicNameValuePair("yt0", "Revoke"));
        String response = doPost(url, headers, expenses.toMap());
        waitForUpdate(3);
        Assert.assertTrue(response.contains("Revoked on:" + expenses.getDate()), "Expense has been revoked successfully");
        return response;
    }

    public String saveExpenseAsDraftAsEmployee(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/applyreimbursement");
        Map headers = new HashMap();

        List<NameValuePair> obj = expenses.toMap();
        obj.add(new BasicNameValuePair("UserExpensesForm[expense_id]", expenses.getExpenseId()));
        obj.add(new BasicNameValuePair("UserExpensesForm[user_id]", expenses.getUserId()));
        obj.add(new BasicNameValuePair("saveasdraft", "SAVE AS DRAFT"));
        String response = doPost(url, headers, obj);
        waitForUpdate(3);
        return response;
    }
    //after save, edit it & add another for existing reimb.take exiting expense draft & add a new expense to it & submit ,

    public String ActionOnExpenseAsAdmin(Expenses expenses, String action) {
        String url = getData("@@url" + "/messages/ReimbursementProcess");
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        List<NameValuePair> obj = expenses.toMap();
        obj.add(new BasicNameValuePair("id", expenses.getExpenseId()));

        if (action.equalsIgnoreCase("Approve"))
            obj.add(new BasicNameValuePair("action", "1"));
        else
            obj.add(new BasicNameValuePair("action", "2"));

        String response = doPost(url, headers, obj);

        return response;
    }

    public String ProcessAndPayExpenseAsAdmin(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/payExpense");
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> obj = expenses.toMap();
        obj.add(new BasicNameValuePair("id", expenses.getExpenseId()));
        obj.add(new BasicNameValuePair("mode", "open_modal"));
        String response = doPost(url, headers, obj);

        if (response.contains("expense_process_pay_form")) {
            obj.add(new BasicNameValuePair("mode", "submit"));
            obj.add(new BasicNameValuePair("expense", expenses.getExpenseId()));
        }
        return response;
    }

    public String RejectExpenseAsAdmin(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/process");
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> obj = expenses.toMap();
        obj.add(new BasicNameValuePair("id", expenses.getExpenseId()));
        obj.add(new BasicNameValuePair("action", "2"));

        String response = doPost(url, headers, obj);
        return response;

    }

    //when does tenant change?
    public String getAllExpensesOnFiltersByEmployee(String status, String month) {

        String tab = null;
        Expenses expenses = new Expenses();
        Map headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        switch (status) {
            case "Approved":
                tab = "0";
                break;
            case "Pending":
                tab = "1";
                break;
            case "Rejected":
                tab = "2";
                break;
            case "Processed":
                tab = "3";
                break;
            case "Draft":
                tab = "5";
                break;
        }
        List<NameValuePair> obj = new ArrayList<>();
        //obj.add(new BasicNameValuePair("tenant","0"));
        //obj.add(new BasicNameValuePair("user", expenses.getUserId()));
        String url = getData("@@url") + "/expenses/expenses/tabfilternew?tab=" + tab + "&user=" + expenses.getUserId() + "&tenant=0" + "&month=" + month;
        String response = doGet(url, headers);
        /*JSONObject objResponse = new JSONObject(response);
        JSONArray jsonArray = objResponse.getJSONArray("status");
        for(Object obj : jsonArray) {
            JSONArray data = (JSONArray) obj;    }*/
    return response;
    }
}


