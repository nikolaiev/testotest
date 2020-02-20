package com.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Data
public class InputTaskDto {

    public static HashMap< Integer, Library> libsMap = new HashMap<>();
    int bookCountTotal;
    int librariesCount;
    int daysToScan;


    HashMap<Integer, Integer> booksRateScore;
    List<Library> libraries;
}
