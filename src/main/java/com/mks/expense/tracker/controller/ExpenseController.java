package com.mks.expense.tracker.controller;

import com.mks.expense.tracker.dto.request.ExpenseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


public interface ExpenseController {

    @PostMapping("/insertExpense")
    public ResponseEntity<Map<String,Object>> insertExpenseDetails(@RequestBody ExpenseRequest expenseRequest);

    @GetMapping("/fetchExpenseDetails")
    public ResponseEntity<Map<String,Object>> fetchExpenseDetails(@RequestParam("mobileNumber") String mobileNumber);
    
    
}
