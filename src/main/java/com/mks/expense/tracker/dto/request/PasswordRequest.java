package com.mks.expense.tracker.dto.request;

import lombok.Data;

@Data
public class PasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String mobileNumber;
}
