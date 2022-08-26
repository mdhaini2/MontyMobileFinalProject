package com.lms.LibraryManagementSystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String author;
    private int quantity;
    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private Set<UserReserveBook> userReservedBooks  = new HashSet<UserReserveBook>();

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Set<UserReserveBook> getUserReservedBooks() {
        return userReservedBooks;
    }

    public void setUserReservedBooks(Set<UserReserveBook> userReservedBooks) {
        this.userReservedBooks = userReservedBooks;
    }
}
