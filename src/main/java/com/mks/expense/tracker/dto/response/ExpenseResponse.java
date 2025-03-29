package com.mks.expense.tracker.dto.response;

import com.mks.expense.tracker.dto.ExpenseDTO;
import com.mks.expense.tracker.dto.UserDTO;
import com.mks.expense.tracker.entity.Expense;
import lombok.Data;

import java.util.List;

@Data
public class ExpenseResponse {
    private UserDTO userDetails;
    private List<ExpenseDTO> listofExpense;
    private float totalExpense;
}
