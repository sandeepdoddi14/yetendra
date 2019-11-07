package com.darwinbox.reimbursement.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.reimbursement.objects.ReimbCreation.Expenses;

import java.util.HashMap;
import java.util.Map;

public class ReimbExpenseService extends Services {

    public void addExpenseAsEmployee(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/applyreimbursement");
        Map headers = new HashMap();
        headers.put("x-frame-options", "sameorigin");
        doPost(url, headers, expenses.toMap());
        //yt1: Submit
    }

    public void getExpenseAsEmployee(Expenses expenses) {
        String url = getData("@url" + "/expenses/expenses/getFormFieldsTypeWise");
        Map headers = new HashMap();
        headers.put("x-frame-options", "sameorigin");
        doGet(url, headers);
    }

    public void editExpenseAsEmployee(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/applyreimbursement");
        Map headers = new HashMap();
        headers.put("x-frame-options", "sameorigin");
        doPost(url, headers, expenses.toMap());
        //yt0: Submit
    }

    public void revokeExpenseAsEmployee(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/DetailedView");
        Map headers = new HashMap();
        headers.put("x-frame-options", "sameorigin");
        doPost(url, headers, expenses.toMap());
        //yt0: Revoke
    }

    public void saveExpenseAsDraftAsEmployee(Expenses expenses) {
        String url = getData("@@url" + "/expenses/expenses/applyreimbursement");
        Map headers = new HashMap();
        headers.put("x-frame-options", "sameorigin");
        doPost(url, headers, expenses.toMap());
        //saveasdraft: SAVE AS DRAFT
    }
}
