package com.lms.User.Repositories;

import com.lms.User.Entities.UserReturnBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnBookRepository extends JpaRepository<UserReturnBook, Integer> {

    public List<UserReturnBook> findByUserId(int userId);
}
