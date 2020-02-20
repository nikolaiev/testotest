package com.dto;

import lombok.Data;

import java.util.List;

@Data
public class InputTaskDto {

    int bookCountTotal;
    int librariesCount;
    int daysToScan;


    int[] booksRateScore;
    List<Library> libraries;
}
