package com.mks.expense.tracker.dto.request;

import lombok.Data;

@Data
public class ExpenseRequest {

    private float total;
    private String comment;
    private String mobileNumber;
}
