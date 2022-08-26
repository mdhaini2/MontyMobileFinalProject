package com.lms.LibraryManagementSystem.Producers.DataProducer;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class UserReturnBookMessage implements Serializable {
    private int userId;
    private int bookId;
    private Date reservedDate;
    private Date returnedDate;

    public UserReturnBookMessage(int userId, int bookId, Date reservedDate, long returnedDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.reservedDate = reservedDate;
        setReturnedDate(returnedDate);
    }

    public UserReturnBookMessage() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Date getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(Date reservedDate) {
        this.reservedDate = reservedDate;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(long timeStamp) {
        Timestamp ts =new Timestamp(timeStamp);
        Date date=new Date(ts.getTime());
        this.returnedDate = date;
    }
}
