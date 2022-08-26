package com.lms.User.Entities;

import javax.persistence.*;
import java.io.Serializable;

@Cacheable
public class OTP implements Serializable {

    private int id;
    private int userId;
    private int otp;

    public OTP()  {
    }

    public OTP(int userId, int otp) {
        this.userId = userId;
        this.otp = otp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "OTP{" +
                "id=" + id +
                ", userId=" + userId +
                ", otp=" + otp +
                '}';
    }
}
