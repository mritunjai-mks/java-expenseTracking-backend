package com.mks.expense.tracker.controller.impl;

import com.mks.expense.tracker.controller.ExpenseController;
import com.mks.expense.tracker.dto.request.ExpenseRequest;
import com.mks.expense.tracker.service.ExpenseService;
import com.mks.expense.tracker.utils.ExpenseUtilConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping(value = "/expense")
public class ExpenseControllerImpl implements ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @Override
    public ResponseEntity<Map<String, Object>> insertExpenseDetails(ExpenseRequest expenseRequest) {
        try {
            Map<String, Object> response = new HashMap<>();

            if (expenseRequest == null || expenseRequest.getComment() == null) {
                response.put("message", "Comment is missing");
                response.put("status", 400);
                return ResponseEntity.badRequest().body(response);
            }

            response = expenseService.insertExpenseDetails(expenseRequest);

            if (response == null || response.isEmpty()) {
                response = new HashMap<>();
                response.put("message", "Something went wrong.");
                response.put("status", 500);
                return ResponseEntity.internalServerError().body(response);
            }

            if (ExpenseUtilConstant.SUCCESS_STATUS.equals(response.get(ExpenseUtilConstant.STATUS))) {
                response.put(ExpenseUtilConstant.MESSAGE, "Expense Created Successfully for loginMobileNumber : " + expenseRequest.getMobileNumber());
                response.put("status", 200);
                return ResponseEntity.ok(response);
            } else if (ExpenseUtilConstant.FAIL.equals(response.get(ExpenseUtilConstant.STATUS))) {
                return ResponseEntity.internalServerError().body(response);
            } else {
                response.put("message", "Please try again later.");
                response.put("status", 500);
                return ResponseEntity.internalServerError().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Unexpected error occurred.");
            errorResponse.put("status", 500);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> fetchExpenseDetails(String mobileNumber) {
        try {
            Map<String, Object> response = new HashMap<>();
            if (mobileNumber == null ) {
                response.put("message", "Mobile Number is missing");
                response.put("status", 400);
                return ResponseEntity.badRequest().body(response);
            }

            response = expenseService.fetchExpenseDetails(mobileNumber);

            if (response == null || response.isEmpty()) {
                response = new HashMap<>();
                response.put("message", "Something went wrong.");
                response.put("status", 500);
                return ResponseEntity.internalServerError().body(response);
            }

            if (ExpenseUtilConstant.SUCCESS_STATUS.equals(response.get(ExpenseUtilConstant.STATUS))) {
                response.put(ExpenseUtilConstant.DETAILS, response.get("details"));
                response.put("status", 200);
                return ResponseEntity.ok(response);
            } else if (ExpenseUtilConstant.FAIL.equals(response.get(ExpenseUtilConstant.STATUS))) {
                return ResponseEntity.internalServerError().body(response);
            } else {
                response.put("message", "Please try after sometime");
                response.put("status", 500);
                return ResponseEntity.internalServerError().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Unexpected error occurred.");
            errorResponse.put("status", 500);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
