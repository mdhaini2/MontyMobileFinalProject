package com.lms.LibraryManagementSystem.Controllers;

import com.google.i18n.phonenumbers.NumberParseException;
import com.lms.LibraryManagementSystem.Entities.Book;
import com.lms.LibraryManagementSystem.Entities.Users;
import com.lms.LibraryManagementSystem.Exceptions.BooksNotFoundException;
import com.lms.LibraryManagementSystem.Exceptions.PhoneNumberInvalidException;
import com.lms.LibraryManagementSystem.Exceptions.UserAlreadyReservedBookException;
import com.lms.LibraryManagementSystem.Services.BooksServices;
import com.lms.LibraryManagementSystem.Services.UserServices;
import com.lms.LibraryManagementSystem.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.JobKOctets;

@RestController
@RequestMapping("/book")
public class BooksController {

    @Autowired
    private BooksServices booksServices;

    @PostMapping(value = "/addBook")
    public Response registerUser(@RequestBody Book book) {
        return booksServices.addBook(book);
    }

    @GetMapping(value = "/getBooksByName")
        public Response getBooksByName(@RequestParam String bookName) throws BooksNotFoundException {
        return booksServices.getBooksByName(bookName);
    }

    @PostMapping(value = "/reserveBook")
    public Response reserveBook(@RequestParam int bookId) throws UserAlreadyReservedBookException, BooksNotFoundException {
        return booksServices.reserveBook(bookId);
    }
    @GetMapping(value = "/getBookByAuthor")
    public Response getBookByAuthor(@RequestParam String author) throws BooksNotFoundException {
        return booksServices.getBookByAuthor(author);
    }
    @GetMapping(value = "/returnBook")
    public Response returnBook(@RequestParam int bookId) throws BooksNotFoundException {
        return booksServices.returnBook(bookId);
    }
}
