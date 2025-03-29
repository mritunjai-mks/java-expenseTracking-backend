package com.mks.expense.tracker.service.impl;

import com.mks.expense.tracker.dao.UserDAO;
import com.mks.expense.tracker.dto.request.PasswordRequest;
import com.mks.expense.tracker.dto.request.UserRequest;
import com.mks.expense.tracker.entity.Users;
import com.mks.expense.tracker.service.UsersService;
import com.mks.expense.tracker.utils.ExpenseUtilConstant;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UserDAO userDAO;

    @Transactional
    public Users getActiveUserByMobile(String mobileNumber) {
        return userDAO.findMobileNumberActiveUser(mobileNumber);
    }

    @Override
    public Map<String, Object> createUser(UserRequest userRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String validationMessage = inputRequestValidation(userRequest);

            if (!ExpenseUtilConstant.VALID.equals(validationMessage)) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, validationMessage);
                return response;
            }

            Users existingUser = getActiveUserByMobile(userRequest.getMobileNumber());
            if (existingUser != null) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, ExpenseUtilConstant.MOBILE_ALREADY_EXISTS + userRequest.getMobileNumber());
                return response;
            }

            int insertedRows = userDAO.insertUser(
                    userRequest.getFirstName(),
                    userRequest.getLastName(),
                    userRequest.getEmailId(),
                    userRequest.getDob(),
                    userRequest.getMobileNumber(),
                    userRequest.getPassword(),
                    true);

            if (insertedRows > 0) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.SUCCESS_STATUS);
            } else {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, "Failed to create user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
            response.put(ExpenseUtilConstant.MESSAGE, "Unexpected error occurred while creating user");
        }
        return response;
    }

    @Override
    public Map<String, Object> updateUserProfile(UserRequest updateRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String validationMessage = updateRequestValidation(updateRequest);

            if (!ExpenseUtilConstant.VALID.equals(validationMessage)) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, validationMessage);
                return response;
            }

            Users existingUser = userDAO.findMobileNumberActiveUser(updateRequest.getMobileNumber());
            if (existingUser == null) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, ExpenseUtilConstant.MOBILE_NOT_FOUND + updateRequest.getMobileNumber());
                return response;
            }

            existingUser = replaceObject(existingUser, updateRequest);

            int updateRow = userDAO.updateUserDetails(existingUser.getUserId(), existingUser.getFirstName(), existingUser.getLastName(), existingUser.getDob(), existingUser.getEmailId());


            if (updateRow > 0) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.SUCCESS_STATUS);
            } else {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, "Failed to create user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
            response.put(ExpenseUtilConstant.MESSAGE, "Unexpected error occurred while updating user");
        }
        return response;
    }

    @Override
    public Map<String, Object> updateUserPassword(PasswordRequest passwordRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String validationMessage = updateRequestPasswordValidation(passwordRequest);

            if (!ExpenseUtilConstant.VALID.equals(validationMessage)) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, validationMessage);
                return response;
            }

            Users existingUser = userDAO.findMobileNumberActiveUser(passwordRequest.getMobileNumber());
            if (existingUser == null) {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, ExpenseUtilConstant.MOBILE_NOT_FOUND + passwordRequest.getMobileNumber());
                return response;
            }

            if (existingUser.getPassword().equals(passwordRequest.getOldPassword())) {
                UserRequest userRequest = new UserRequest();
                userRequest.setPassword(passwordRequest.getNewPassword());
                existingUser = replaceObject(existingUser, userRequest);
                int updateRow = userDAO.updateUserPassword(existingUser.getUserId(), existingUser.getPassword());
                if (updateRow > 0) {
                    response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.SUCCESS_STATUS);
                } else {
                    response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                    response.put(ExpenseUtilConstant.MESSAGE, "Failed to create user");
                }
            } else {
                response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
                response.put(ExpenseUtilConstant.MESSAGE, "Enter User/Password is Wrong. Please try after sometime.");

            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ExpenseUtilConstant.STATUS, ExpenseUtilConstant.FAIL);
            response.put(ExpenseUtilConstant.MESSAGE, "Unexpected error occurred while updating user");
        }
        return response;
    }

    private String updateRequestPasswordValidation(PasswordRequest updateRequest) {
        if (updateRequest == null) {
            return "User request cannot be null";
        }
        if (updateRequest.getOldPassword() == null) {
            return "Old Password must be enter !";
        }
        if (updateRequest.getNewPassword() == null && !(updateRequest.getNewPassword() instanceof String)) {
            return "Please enter the valid Password.";
        }
        if (updateRequest.getMobileNumber() != null) {
            if (!(updateRequest.getMobileNumber() instanceof String) || !updateRequest.getMobileNumber().matches("\\d{10}")) {
                return "Mobile number should be a 10-digit numeric value.";
            }
        }
        return ExpenseUtilConstant.VALID;
    }

    private Users replaceObject(Users existingUser, UserRequest updateRequest) {
        if (updateRequest.getFirstName() != null) {
            existingUser.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            existingUser.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getDob() != null) {
            String dob = updateRequest.getDob().trim();

            // Append time if only date is provided
            if (dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
                dob += " 00:00:00";  // Default time
            }

            try {
                existingUser.setDob(Timestamp.valueOf(dob));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
            }
        }
        if (updateRequest.getEmailId() != null) {
            existingUser.setEmailId(updateRequest.getEmailId());
        }
        if(updateRequest.getPassword()!=null){
            existingUser.setPassword(updateRequest.getPassword());
        }
        return existingUser;
    }

    private String updateRequestValidation(UserRequest updateRequest) {
        if (updateRequest == null) {
            return "User request cannot be null";
        }
        if (updateRequest.getFirstName() == null && updateRequest.getLastName() == null && updateRequest.getEmailId() == null && updateRequest.getDob() == null && updateRequest.getMobileNumber() == null) {
            return "At least one field (firstName, lastName, emailId, dob, mobileNumber) must be provided.";
        }
        if (updateRequest.getFirstName() != null) {
            if (!(updateRequest.getFirstName() instanceof String) || updateRequest.getFirstName().trim().isEmpty()) {
                return "First name must be a non-empty string.";
            }
        }
        if (updateRequest.getLastName() != null) {
            if (!(updateRequest.getLastName() instanceof String) || updateRequest.getLastName().trim().isEmpty()) {
                return "Last name must be a non-empty string.";
            }
        }
        if (updateRequest.getEmailId() != null) {
            if (!(updateRequest.getEmailId() instanceof String) || !updateRequest.getEmailId().matches("^[A-Za-z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
                return "Invalid email format.";
            }
        }
        if (updateRequest.getDob() != null) {
            if (!(updateRequest.getDob() instanceof String) || !updateRequest.getDob().matches("\\d{4}-\\d{2}-\\d{2}")) {
                return "Invalid date of birth format (expected YYYY-MM-DD).";
            }
        }
        if (updateRequest.getMobileNumber() != null) {
            if (!(updateRequest.getMobileNumber() instanceof String) || !updateRequest.getMobileNumber().matches("\\d{10}")) {
                return "Mobile number should be a 10-digit numeric value.";
            }
        }
        return ExpenseUtilConstant.VALID;
    }


    private String inputRequestValidation(UserRequest userRequest) {
        if (userRequest == null) {
            return "User request cannot be null";
        }
        if (userRequest.getFirstName() == null || userRequest.getFirstName().trim().isEmpty()) {
            return "First name is required";
        }
        if (userRequest.getLastName() == null || userRequest.getLastName().trim().isEmpty()) {
            return "Last name is required";
        }
        if (userRequest.getEmailId() == null || !userRequest.getEmailId().matches("^[A-Za-z0-9+_.-]+@[a-zA-Z0-9.-]+$") ) {
            return "Invalid email format";
        }
        if (userRequest.getDob() == null || !userRequest.getDob().matches("\\d{4}-\\d{2}-\\d{2}")) {
            return "Invalid date of birth format (expected YYYY-MM-DD)";
        }
        if (userRequest.getMobileNumber() == null || !userRequest.getMobileNumber().matches("\\d{10}")) {
            return "Mobile number should be 10 digits";
        }
        if(userRequest.getPassword() == null){
            return "Password is missing";
        }
        return ExpenseUtilConstant.VALID;
    }
}
