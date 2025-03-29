package com.mks.expense.tracker.service.impl;

import com.mks.expense.tracker.dao.ExpenseDAO;
import com.mks.expense.tracker.dao.UserDAO;
import com.mks.expense.tracker.dto.ExpenseDTO;
import com.mks.expense.tracker.dto.UserDTO;
import com.mks.expense.tracker.dto.request.ExpenseRequest;
import com.mks.expense.tracker.dto.response.ExpenseResponse;
import com.mks.expense.tracker.entity.Expense;
import com.mks.expense.tracker.entity.Users;
import com.mks.expense.tracker.service.ExpenseService;
import com.mks.expense.tracker.utils.ExpenseUtilConstant;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    ExpenseDAO expenseDAO;

    @Transactional
    public Users getActiveUserByMobile(String mobileNumber) {
        return userDAO.findMobileNumberActiveUser(mobileNumber);
    }

    @Override
    public Map<String, Object> insertExpenseDetails(ExpenseRequest expenseRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String validationMessage = inputRequestValidation(expenseRequest);

            if (!ExpenseUtilConstant.VALID.equals(validationMessage)) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, validationMessage);
                return response;
            }

            Users existingUser = getActiveUserByMobile(expenseRequest.getMobileNumber());
            if (existingUser == null) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, ExpenseUtilConstant.MOBILE_NOT_FOUND + expenseRequest.getMobileNumber());
                return response;
            }

            int insertedRows = expenseDAO.insertExpenseDetails(existingUser.getUserId(), expenseRequest.getTotal(), expenseRequest.getComment());

            if (insertedRows > 0) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.SUCCESS_STATUS);
            } else {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, "Failed to insert expense record");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
            response.put(ExpenseUtilConstant.MESSAGE, "Unexpected error occurred while creating expense");
        }
        return response;
    }

    @Override
    public Map<String, Object> fetchExpenseDetails(String mobileNumber) {
        Map<String, Object> response = new HashMap<>();
        try {
            Users existingUser = getActiveUserByMobile(mobileNumber);
            if (existingUser == null) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, ExpenseUtilConstant.MOBILE_NOT_FOUND + mobileNumber);
                return response;
            }

            List<Expense> expenseResult = expenseDAO.fetchExpenseDetails(existingUser.getUserId());

            if (expenseResult != null && !expenseResult.isEmpty()) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.SUCCESS_STATUS);
                ExpenseResponse expenseResponse = prepareResponse(existingUser, expenseResult);
                response.put(ExpenseUtilConstant.DETAILS, expenseResponse); // Store only under "details"
            } else {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, "No expense records found for this user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
            response.put(ExpenseUtilConstant.MESSAGE, "Unexpected error occurred while fetching expense");
        }
        return response;
    }

    private ExpenseResponse prepareResponse(Users existingUser, List<Expense> expenseResult) {
        ExpenseResponse expenseResponse = new ExpenseResponse();
        List<ExpenseDTO> listofExpense = new ArrayList<>();
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(existingUser.getFirstName() != null ? existingUser.getFirstName() : null);
        userDTO.setLastName(existingUser.getLastName() != null ? existingUser.getLastName() : null);
        userDTO.setEmailId(existingUser.getEmailId() != null ? existingUser.getEmailId() : null);
        userDTO.setMobileNumber((existingUser.getMobileNumber() != null ? existingUser.getMobileNumber() : null));
        float sumTotalPrice= 0.0F;
        for (Expense e : expenseResult) {
            ExpenseDTO expenseDTO = new ExpenseDTO();
            expenseDTO.setComment(e.getComment() != null ? e.getComment() : null);
            expenseDTO.setPrice(e.getTotal());
            sumTotalPrice+=e.getTotal();
            listofExpense.add(expenseDTO);
        }
        expenseResponse.setTotalExpense(sumTotalPrice);
        expenseResponse.setListofExpense(listofExpense);
        expenseResponse.setUserDetails(userDTO);
        return expenseResponse;

    }

    private String inputRequestValidation(ExpenseRequest expenseRequest) {
        if (expenseRequest == null) {
            return "Request Must be pass.";
        }
        if (expenseRequest.getComment() == null) {
            if (expenseRequest.getComment() instanceof String) {
                return "Please enter the correct comment";
            }
        }
        if (expenseRequest.getTotal() == 0.0 || expenseRequest.getTotal() < 0) {
            if (expenseRequest.getTotal() < 0) {
                return "Total cant be feed in negative.";
            }
        }
        return ExpenseUtilConstant.VALID;
    }
}
