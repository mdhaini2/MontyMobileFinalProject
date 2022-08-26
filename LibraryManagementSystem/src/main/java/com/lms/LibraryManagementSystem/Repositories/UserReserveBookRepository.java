package com.lms.LibraryManagementSystem.Repositories;

import com.lms.LibraryManagementSystem.Entities.UserReserveBook;
import com.lms.LibraryManagementSystem.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReserveBookRepository  extends JpaRepository<UserReserveBook, Integer> {
}
