package com.lms.LibraryManagementSystem.Repositories;

import com.lms.LibraryManagementSystem.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository  extends JpaRepository<Book, Integer> {


//    @Query(value = "Select id, name, author, quantity,  from Book where author = ?1")
//    public Object findBooksByAuthor(String author);

    public Object findByNameStartingWith(String bookName);
    @Query(value = "Select * from book where name ilike %:bookName% ",nativeQuery = true)
    public List<Book> findByNameLike(String bookName);
    @Query(value = "Select * from book where author ilike %:author% ",nativeQuery = true)
    public List<Book> getBooksByAuthor(String author);
}
