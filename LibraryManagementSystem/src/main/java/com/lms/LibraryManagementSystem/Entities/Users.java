package com.lms.LibraryManagementSystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;
    private String fullName;
    private String email;
    @Column(unique = true)
    private String phoneNumber;
    private String address;
    private String password;
    private int isRegistered;

    private double returnAvg;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<UserReserveBook> userReservedBooks  = new HashSet<UserReserveBook>();

    public Users() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(int isRegistered) {
        this.isRegistered = isRegistered;
    }

    public Set<UserReserveBook> getUserReservedBooks() {
        return userReservedBooks;
    }

    public void setUserReservedBooks(Set<UserReserveBook> userReservedBooks) {
        this.userReservedBooks = userReservedBooks;
    }

    public double getReturnAvg() {
        return returnAvg;
    }

    public void setReturnAvg(double returnAvg) {
        this.returnAvg = returnAvg;
    }
}
