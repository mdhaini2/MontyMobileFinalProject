package com.lms.LibraryManagementSystem.Services;

import com.lms.LibraryManagementSystem.Entities.Book;
import com.lms.LibraryManagementSystem.Entities.UserReserveBook;
import com.lms.LibraryManagementSystem.Entities.Users;
import com.lms.LibraryManagementSystem.Exceptions.BooksNotFoundException;
import com.lms.LibraryManagementSystem.Exceptions.UserAlreadyReservedBookException;
import com.lms.LibraryManagementSystem.Producers.DataProducer.UserReturnBookMessage;
import com.lms.LibraryManagementSystem.Producers.ProducerUserReturnBook;
import com.lms.LibraryManagementSystem.Repositories.BooksRepository;
import com.lms.LibraryManagementSystem.Repositories.UserRepository;
import com.lms.LibraryManagementSystem.Repositories.UserReserveBookRepository;
import com.lms.LibraryManagementSystem.Utils.JwtUtil;
import com.lms.LibraryManagementSystem.Utils.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@AllArgsConstructor
@Service
public class BooksServices {
    @Autowired
    BooksRepository booksRepository;
    @Autowired
    UserReserveBookRepository userReserveBookRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProducerUserReturnBook producerUserReturnBook;

    @Autowired
    private JwtUtil jwtTokenUtil;
    private HttpServletRequest request;

    public Response addBook(Book book) {
        log.info("Adding new book to DB");
        booksRepository.save(book);
        String message = "Book added successfully";
        Response response = new Response(message, book);
        return response;
    }

    public Response getBooksByName(String bookName) throws BooksNotFoundException {
        log.info("Retrieving list of books with name like: " + bookName);
        List<Book> bookList = booksRepository.findByNameLike(bookName);

        log.info("Book lists with name like :" + bookName + " isNull: " + bookList.isEmpty());
        if (!bookList.isEmpty()) {
            Response response = new Response("List of books retrieved", bookList);
            return response;
        }
        log.error("Books with name like " + bookName + " not found!");
        throw new BooksNotFoundException("Books with name like " + bookName + " not found!");

    }

    public Response reserveBook(int bookId) throws UserAlreadyReservedBookException, BooksNotFoundException {
        log.info("Get user from token");
        String token = jwtTokenUtil.getToken(request);
        String phoneNumber = jwtTokenUtil.extractUsername(token);

        Users user = userRepository.findByPhoneNumber(phoneNumber);
        Set<UserReserveBook> userReserveBooks = user.getUserReservedBooks();
        log.info("Checking if user already has a book reserved");
        for (UserReserveBook userReserveBook : userReserveBooks) {
            if (userReserveBook.getStatus().equalsIgnoreCase("reserved")) {
                log.error(user.getFullName() + " already reserved the book: " + userReserveBook.getBook().getName());
                throw new UserAlreadyReservedBookException(user.getFullName() + " already reserved the book: " + userReserveBook.getBook().getName());
            }
        }
        log.info("Searching for a book with bookID: " + bookId);
        Optional<Book> optionalBook = booksRepository.findById(bookId);
        if (optionalBook == null) {
            log.error("Book with id: " + bookId + " not found");
            throw new BooksNotFoundException("Book with id: " + bookId + " not found");
        }

        Book book = optionalBook.get();

        if (book.getQuantity() == 0) {
            log.error(book.getName() + " quantity = " + book.getQuantity());
            throw new BooksNotFoundException(book.getName() + " has no more copies left");
        }

        UserReserveBook userReserveBook = new UserReserveBook(user, book, System.currentTimeMillis(), "reserved");
        log.info("Add new book reservation to the users book reservation list");
        user.getUserReservedBooks().add(userReserveBook);

        log.info("Decrementing the quantity of the book " + book.getName());
        book.setQuantity(book.getQuantity() - 1);
        book.getUserReservedBooks().add(userReserveBook);

        log.info("Saving updated data to DB");
        userReserveBookRepository.save(userReserveBook);
        userRepository.save(user);
        booksRepository.save(book);

        Response response = new Response("User reserved " + book.getName(), userReserveBook);
        return response;
    }

    public Response getBookByAuthor(String author) throws BooksNotFoundException {
        log.info("Get list of books with author name like: " + author);
        List<Book> bookList = booksRepository.getBooksByAuthor(author);
        if (bookList.isEmpty()) {
            log.error("The author " + author + " has no books in LMS");
            throw new BooksNotFoundException("The author " + author + " has no books in LMS");
        }
        Response response = new Response("Books by author " + author + " retrieved", bookList);
        return response;
    }

    public Response returnBook(int bookId) throws BooksNotFoundException {
        log.info("Get a book from DB with bookID: " + bookId);
        Optional<Book> optionalBook = booksRepository.findById(bookId);

        if (optionalBook.isEmpty()) {
            log.error("Book with id" + bookId + " not found!");
            throw new BooksNotFoundException("Book with id" + bookId + " not found!");
        }

        Book book = optionalBook.get();

        log.info("Get user from token");
        String token = jwtTokenUtil.getToken(request);
        String phoneNumber = jwtTokenUtil.extractUsername(token);
        Users user = userRepository.findByPhoneNumber(phoneNumber);

        Set<UserReserveBook> userReserveBooks = user.getUserReservedBooks();
        log.info("Making sure the book with bookID " + bookId + " is reserved by the user");
        for (UserReserveBook userReserveBook : userReserveBooks) {
            if (userReserveBook.getBook().getId() == bookId) {
                log.info("Book reservation from the user confirmed");

                if (userReserveBook.getStatus().equalsIgnoreCase("reserved")) {
                    log.info("updating data to the DB");

                    userReserveBook.setStatus("returned");
                    userReserveBookRepository.save(userReserveBook);

                    user.setUserReservedBooks(userReserveBooks);
                    userRepository.save(user);

                    book.setQuantity(book.getQuantity() + 1);
                    booksRepository.save(book);

                    UserReturnBookMessage userReturnBookMessage = new UserReturnBookMessage(user.getUserID(), bookId, userReserveBook.getDate(), System.currentTimeMillis());
                    producerUserReturnBook.sendJsonMessage(userReturnBookMessage);

                    Response response = new Response("sent return confirmation to user", userReturnBookMessage);
                    return response;
                }
                log.error("Book already returned by the user");

                throw new BooksNotFoundException("Book already returned");
            }
        }
        log.error("The user did not reserve the book" + book.getName() + " with id:" + bookId);

        throw new BooksNotFoundException("The user did not reserve the book" + book.getName() + " with id:" + bookId);

    }
}
