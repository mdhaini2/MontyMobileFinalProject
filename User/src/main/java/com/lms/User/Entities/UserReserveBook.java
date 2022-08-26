package com.lms.User.Entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class UserReserveBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;

    @ManyToOne
    @JoinColumn(name = "users")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "book")
    private Book book;

    private Date date;
    private String status;

    public UserReserveBook() {
    }

    public UserReserveBook(Users user, Book book, long timestamp, String status) {
        this.user = user;
        this.book = book;
        setDate(timestamp);
        this.status = status;
    }


    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(long timeStamp) {
        Timestamp ts =new Timestamp(timeStamp);
        Date date=new Date(ts.getTime());
        this.date = date;;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
