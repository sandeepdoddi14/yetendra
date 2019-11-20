package com.darwinbox.reimbursement;

import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.reimbursement.objects.ReimbCreation.Expenses;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import com.darwinbox.reimbursement.services.ReimbFormService;

public class TestFailureOnCreatingAnExpenseAsEmployee {
    LoginPage loginPage;
    Expenses expenses;
    ReimbFormService reimbFormService;
    ReimbForm reimbForm;
    CommonAction commonAction;
//createResponse.contains("failure"||"You can apply this reimbursement")

}
