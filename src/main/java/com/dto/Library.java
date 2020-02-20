package com.dto;

import lombok.Data;

import java.util.List;

@Data
public class Library {

    int number;
    int booksCount;
    int signUpDays;
    int bookPerDay;

    List<Book> books;


}
