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
        booksRepository.save(book);
        String message = "Book added successfully";
        Response response = new Response(message, book);
        return response;
    }

    public Object getBooksByName(String bookName) throws BooksNotFoundException {
        List<Book> bookList = booksRepository.findByNameLike(bookName);
        log.info(bookList.isEmpty());
        if (!bookList.isEmpty()) {
            Response response = new Response("List of books retrieved", bookList);
            return response;
        }

        throw new BooksNotFoundException("Books with name like " + bookName + " not found!");

    }

    public Response reserveBook(int bookId) throws UserAlreadyReservedBookException, BooksNotFoundException {
        String token = jwtTokenUtil.getToken(request);
        String phoneNumber = jwtTokenUtil.extractUsername(token);


        Users user = userRepository.findByPhoneNumber(phoneNumber);
        Set<UserReserveBook> userReserveBooks = user.getUserReservedBooks();

        for (UserReserveBook userReserveBook : userReserveBooks) {
            if (userReserveBook.getStatus().equalsIgnoreCase("reserved")) {
                throw new UserAlreadyReservedBookException(user.getFullName() + " already reserved the book: " + userReserveBook.getBook().getName());
            }
        }

        Optional<Book> optionalBook = booksRepository.findById(bookId);
        if (optionalBook == null) {
            throw new BooksNotFoundException("Book with id: " + bookId + " not found");
        }

        Book book = optionalBook.get();

        if (book.getQuantity() == 0) {
            log.error(book.getName() + " quantity = " + book.getQuantity());
            throw new BooksNotFoundException(book.getName() + " has no more copies left");
        }

        UserReserveBook userReserveBook = new UserReserveBook(user, book, System.currentTimeMillis(), "reserved");

        user.getUserReservedBooks().add(userReserveBook);

        book.setQuantity(book.getQuantity() - 1);
        book.getUserReservedBooks().add(userReserveBook);

        userReserveBookRepository.save(userReserveBook);
        userRepository.save(user);
        booksRepository.save(book);

        Response response = new Response("User reserved " + book.getName(), userReserveBook);
        return response;
    }

    public Response getBookByAuthor(String author) throws BooksNotFoundException {
        List<Book> bookList = booksRepository.getBooksByAuthor(author);
        if (bookList.isEmpty()) {
            throw new BooksNotFoundException("The author " + author + " has no books in LMS");
        }
        Response response = new Response("Books by author " + author + " retrieved", bookList);
        return response;
    }

    public Object returnBook(int bookId) throws BooksNotFoundException {
        Optional<Book> optionalBook = booksRepository.findById(bookId);

        if (optionalBook.isEmpty()) {
            throw new BooksNotFoundException("Book with id" + bookId + " not found!");
        }

        Book book = optionalBook.get();

        String token = jwtTokenUtil.getToken(request);
        String phoneNumber = jwtTokenUtil.extractUsername(token);
        Users user = userRepository.findByPhoneNumber(phoneNumber);

        Set<UserReserveBook> userReserveBooks = user.getUserReservedBooks();

        for (UserReserveBook userReserveBook : userReserveBooks) {
            if (userReserveBook.getBook().getId() == bookId) {
                if (userReserveBook.getStatus().equalsIgnoreCase("reserved")) {

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
                throw new BooksNotFoundException("Book already returned");
            }
        }
        throw new BooksNotFoundException("The user did not reserve the book" + book.getName() + " with id:" + bookId);

    }
}
