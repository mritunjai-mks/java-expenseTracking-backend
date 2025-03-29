package com.mks.expense.tracker.controller.impl;

import com.mks.expense.tracker.controller.UsersController;
import com.mks.expense.tracker.dto.request.PasswordRequest;
import com.mks.expense.tracker.dto.request.UserRequest;
import com.mks.expense.tracker.service.UsersService;
import com.mks.expense.tracker.utils.ExpenseUtilConstant;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "User Controller", description = "Operation Related to User Creation")
public class UsersControllerImpl implements UsersController {

    @Autowired
    UsersService usersService;

    @Override
    public ResponseEntity<Map<String, Object>> createUser(UserRequest userRequest) {
        try {
            Map<String, Object> response = new HashMap<>();

            if (userRequest == null || userRequest.getMobileNumber() == null) {
                response.put("message", "Mobile number is missing");
                response.put("status", 400);
                return ResponseEntity.badRequest().body(response);
            }

            response = usersService.createUser(userRequest);

            if (response == null || response.isEmpty()) {
                response = new HashMap<>();
                response.put("message", "Something went wrong.");
                response.put("status", 500);
                return ResponseEntity.internalServerError().body(response);
            }

            if (ExpenseUtilConstant.SUCCESS_STATUS.equals(response.get(ExpenseUtilConstant.STATUS))) {
                response.put(ExpenseUtilConstant.MESSAGE, "User Created Successfully. Mobile : " + userRequest.getMobileNumber());
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
    public ResponseEntity<Map<String, Object>> updateUserProfile(UserRequest updateRequest) {
        try {
            Map<String, Object> response = new HashMap<>();

            if (updateRequest == null || updateRequest.getMobileNumber() == null) {
                response.put("message", "Mobile number is missing");
                response.put("status", 400);
                return ResponseEntity.badRequest().body(response);
            }
            response = usersService.updateUserProfile(updateRequest);

            if (response == null || response.isEmpty()) {
                response = new HashMap<>();
                response.put("message", "Something went wrong.");
                response.put("status", 500);
                return ResponseEntity.internalServerError().body(response);
            }

            if (ExpenseUtilConstant.SUCCESS_STATUS.equals(response.get(ExpenseUtilConstant.STATUS))) {
                response.put(ExpenseUtilConstant.MESSAGE, "User Updated Successfully. Mobile : " + updateRequest.getMobileNumber());
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
    public ResponseEntity<Map<String, Object>> updateUserPassword(PasswordRequest passwordRequest) {
        try {
            Map<String, Object> response = new HashMap<>();

            if (passwordRequest == null || passwordRequest.getMobileNumber() == null) {
                response.put("message", "Mobile number is missing");
                response.put("status", 400);
                return ResponseEntity.badRequest().body(response);
            }

            response = usersService.updateUserPassword(passwordRequest);

            if (response == null || response.isEmpty()) {
                response = new HashMap<>();
                response.put("message", "Something went wrong.");
                response.put("status", 500);
                return ResponseEntity.internalServerError().body(response);
            }

            if (ExpenseUtilConstant.SUCCESS_STATUS.equals(response.get(ExpenseUtilConstant.STATUS))) {
                response.put(ExpenseUtilConstant.MESSAGE, "User Password Updated Successfully. Mobile : " + passwordRequest.getMobileNumber());
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
}
