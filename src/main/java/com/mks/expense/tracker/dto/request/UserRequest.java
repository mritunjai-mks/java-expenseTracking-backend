package com.mks.expense.tracker.dto.request;

import lombok.Data;

@Data
public class UserRequest {

    private String firstName;

    private String lastName;

    private String emailId;

    private String dob;

    private String mobileNumber;

    private String password;
}
