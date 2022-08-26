package com.lms.User.Producers.DataProducer;

import java.io.Serializable;

public class ConfirmationOTP implements Serializable {
    private int userId;
    private int otp;

    public ConfirmationOTP(int userId, int otp) {
        this.userId = userId;
        this.otp = otp;
    }

    public ConfirmationOTP() {
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
}
