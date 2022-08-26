package com.lms.User.Services;

import com.lms.User.Entities.OTP;
import com.lms.User.Entities.Users;
import com.lms.User.Exceptions.IncorrectOTPException;
import com.lms.User.Exceptions.InvalidUserException;
import com.lms.User.Exceptions.OTPNotReceivedException;
import com.lms.User.Repositories.UserRepository;
import com.lms.User.Utils.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class OTPConfirmationService {
    @Autowired
    UserRepository userRepository;
    public static final String HASH_KEY = "user-";
    @Autowired
    private RedisTemplate redisTemplate;

    public Object confirmOTP(int userId, int otp) throws InvalidUserException, OTPNotReceivedException, IncorrectOTPException {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new InvalidUserException("user with id " + userId + " does not exists!");
        }
        Users user = optionalUser.get();
//        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        int redisOTP = 0;

        String key = HASH_KEY.concat(String.valueOf(userId));
        redisOTP = (int) redisTemplate.opsForValue().get(key);


        if (redisOTP == 0) {
            throw new OTPNotReceivedException("Server did not send an OTP for user: " + user.getFullName());
        }

        if (redisOTP == otp) {
            user.setIsRegistered(1);
            userRepository.save(user);
            Response response = new Response("User registration confirmed!", user);
            return response;
        }
        throw new IncorrectOTPException("Wrong OTP!");
    }
}
