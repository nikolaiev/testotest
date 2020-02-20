package com.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class InputTaskDto {

    int bookCountTotal;
    int librariesCount;
    int daysToScan;


    HashMap<Integer, Integer> booksRateScore;
    List<Library> libraries;
}
