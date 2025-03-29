package com.mks.expense.tracker.service;

import com.mks.expense.tracker.dto.request.ExpenseRequest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface ExpenseService {
    Map<String, Object> insertExpenseDetails(ExpenseRequest expenseRequest);

    Map<String,Object> fetchExpenseDetails(String mobileNumber);

}
