package com.mks.expense.tracker.dao;

import com.mks.expense.tracker.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface UserDAO extends JpaRepository<Users,Long> {


    @Query("SELECT u FROM Users u WHERE u.mobileNumber = :mobileNumber")
    Users findMobileNumber(@Param("mobileNumber") String mobileNumber);

    @Query("SELECT u FROM Users u WHERE u.mobileNumber = :mobileNumber  AND u.isActiveUser=true")
    Users findMobileNumberActiveUser(@Param("mobileNumber") String mobileNumber);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (first_name, last_name, email_id, dob, mobile_number, password, is_active_user, create_date, update_date) " +
            "VALUES (:firstName, :lastName, :email, :dob, :mobileNumber, :password, :isActiveUser, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)", nativeQuery = true)
    int insertUser(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("dob") String dob,
            @Param("mobileNumber") String mobileNumber,
            @Param("password") String password,
            @Param("isActiveUser") boolean isActiveUser
    );

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET " +
            "u.firstName = :firstName, " +
            "u.lastName = :lastName, " +
            "u.dob = :dob, " +
            "u.emailId = :emailId, " +
            "u.updateDate = CURRENT_TIMESTAMP " +
            "WHERE u.userId = :userId")
    int updateUserDetails(
            @Param("userId") int userId,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("dob") Timestamp dob,
            @Param("emailId") String emailId
    );

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET " +
            " u.password = :password, " +
            " u.updateDate = CURRENT_TIMESTAMP " +
            " WHERE u.userId = :userId ")
    int updateUserPassword(@Param("userId") int userId,
                           @Param("password") String password);
}
