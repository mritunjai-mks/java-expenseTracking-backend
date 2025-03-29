package com.mks.expense.tracker.dao;

import com.mks.expense.tracker.entity.Expense;
import com.mks.expense.tracker.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseDAO extends JpaRepository<Expense,Long> {



    @Modifying
    @Transactional
    @Query(value = "INSERT INTO expense (user_id, total, comment, create_date, update_date) " +
            "VALUES (:userId, :total, :comment, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)", nativeQuery = true)
    int insertExpenseDetails(@Param("userId") int userId,
                             @Param("total") float total,
                             @Param("comment") String comment);

    @Modifying
    @Transactional
    @Query("SELECT e FROM Expense e WHERE e.user.userId = :userId")
    List<Expense> fetchExpenseDetails(@Param("userId") int userId);
}
