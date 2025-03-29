package com.mks.expense.tracker.controller;

import com.mks.expense.tracker.dto.request.PasswordRequest;
import com.mks.expense.tracker.dto.request.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


public interface UsersController {

    @PostMapping("/createUser")
    @Operation(summary = "Create User API", description = "Use to create unique userId base on emailId")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserRequest userRequest);

    @PostMapping("/updateUserProfile")
    @Operation(summary = "Update User Profile API", description = "firstName,lastName,dob and email can be update")
    public ResponseEntity<Map<String, Object>> updateUserProfile(@RequestBody UserRequest updateRequest);

    @PostMapping("/updatePassword")
    @Operation(summary = "Update User Password API", description = "Only password can be update")
    public ResponseEntity<Map<String, Object>> updateUserPassword(@RequestBody PasswordRequest updateRequest);
}
