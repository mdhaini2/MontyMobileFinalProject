package com.lms.LibraryManagementSystem.Repositories;

import com.lms.LibraryManagementSystem.Entities.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OTPRepository extends JpaRepository<OTP, Integer> {

    @Query(value = "Select * from OTP where user_id = ?1 ORDER BY ID DESC Limit 1", nativeQuery = true)
    public OTP findLatestOTP(int userId);
}
