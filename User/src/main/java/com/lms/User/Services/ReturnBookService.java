package com.lms.User.Services;

import com.lms.User.Entities.UserReturnBook;
import com.lms.User.Entities.UserReturnBookMessage;
import com.lms.User.Entities.Users;
import com.lms.User.Repositories.ReturnBookRepository;
import com.lms.User.Repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Log4j2
@Service
public class ReturnBookService {
    @Autowired
    ReturnBookRepository returnBookRepository;
    @Autowired
    UserRepository userRepository;

    public void userReturnBook(UserReturnBookMessage userReturnBookMessage) throws ParseException {

        int userId = userReturnBookMessage.getUserId();
        int bookId = userReturnBookMessage.getBookId();
        Date returnedDate = userReturnBookMessage.getReturnedDate();
        Date reservedDate = userReturnBookMessage.getReservedDate();

        UserReturnBook userReturnBook = new UserReturnBook(userId,bookId,reservedDate,returnedDate);
        returnBookRepository.save(userReturnBook);

        List<UserReturnBook> userReturnBookList = returnBookRepository.findByUserId(userId);

        long counter =0;

        for(UserReturnBook userReturnBook1:userReturnBookList){
            reservedDate = userReturnBook1.getReservedDate();
            returnedDate = userReturnBook1.getReturnedDate();
            counter += dateDifference(reservedDate,returnedDate);
        }
        double returnAvg = counter/Double.valueOf(userReturnBookList.size());

        Users user = userRepository.findById(userId).get();

        user.setReturnAvg(returnAvg);
        userRepository.save(user);

    }
    public long dateDifference(Date date1, Date date2) throws ParseException {

        SimpleDateFormat obj = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        log.info("Date1:"+date1);

        date1 = obj.parse(String.valueOf(date1));
        date2 = obj.parse(String.valueOf(date2));

        long timeDifference = date2.getTime() - date1.getTime();
        long daysDifference = (timeDifference / (1000*60*60*24)) % 365;
        long secondsDifference = (timeDifference / 1000)% 60;
        long hours_difference = (timeDifference / (1000*60*60)) % 24;
        log.info(hours_difference);
        return hours_difference;
    }
}
