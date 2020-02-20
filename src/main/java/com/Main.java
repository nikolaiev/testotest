package com;

import com.dto.InputTaskDto;
import com.dto.Library;
import com.utils.FromFileToDtoTrees;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class Main {

    HashSet<Integer> booksProceeded = new HashSet<>();

    public static void main(String[] args) throws IOException {

        InputTaskDto inputTaskDto = FromFileToDtoTrees.getFromFile("a_example.txt");
        final HashMap<Integer, Integer> booksRateScore = inputTaskDto.getBooksRateScore();
        int totalDaysLeft = inputTaskDto.getDaysToScan(); //1000

        List<Integer> resultLibList = new LinkedList<>();
        //TODO one more loop
        while (totalDaysLeft > 0 && !inputTaskDto.getLibraries().isEmpty()) {


            BestLib bestLib = new BestLib();
            int iterationCount = 0;

            for (Library library : inputTaskDto.getLibraries()) {
                    log.info("IterCount {}", iterationCount);
                final int bookPerDay = library.getBookPerDay();
                final int signUpDays = library.getSignUpDays();
                final int daysLeftASfteSignUp = totalDaysLeft - signUpDays;
                if (daysLeftASfteSignUp <= 0) {

                } else {
                    //process
                    final int possibleBookToProcess = daysLeftASfteSignUp * bookPerDay;
                    final int[] bookWillBeProceeded = Arrays.copyOfRange(library.getBooks(), 0, possibleBookToProcess - 1);
                    final long totalRate = Arrays.stream(bookWillBeProceeded).mapToLong(booksRateScore::get).sum();

                    if (totalRate > bestLib.libScore) {
                        bestLib.libId = library.getNumber();
                        bestLib.signUpDay = library.getSignUpDays();
                        bestLib.libScore = totalRate;
                    }
                }

                iterationCount++;
            }

            resultLibList.add(bestLib.libId);


            totalDaysLeft = totalDaysLeft - bestLib.signUpDay;
            inputTaskDto.getLibraries().removeIf(library -> library.getNumber() == bestLib.libId);


        }

        System.out.println(resultLibList);


    }

    static class BestLib {
        int libId;
        long libScore;
        int signUpDay;
    }
}
