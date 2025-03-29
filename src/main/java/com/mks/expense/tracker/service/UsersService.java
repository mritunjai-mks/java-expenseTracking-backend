package com.mks.expense.tracker.service;

import com.mks.expense.tracker.dto.request.PasswordRequest;
import com.mks.expense.tracker.dto.request.UserRequest;
import org.json.JSONObject;

import java.util.Map;

public interface UsersService {
    Map<String,Object> createUser(UserRequest userRequest);

    Map<String, Object> updateUserProfile(UserRequest updateRequest);

    Map<String, Object> updateUserPassword(PasswordRequest updateRequest);
}
