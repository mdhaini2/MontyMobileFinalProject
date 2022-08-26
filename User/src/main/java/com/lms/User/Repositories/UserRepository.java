package com.lms.User.Repositories;

import com.lms.User.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    public Users findByPhoneNumber(String phoneNumber);
}
