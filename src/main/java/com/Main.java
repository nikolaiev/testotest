package com;

import com.dto.InputTaskDto;
import com.dto.Library;
import com.io.FileReaderWriter;
import com.utils.FromFileToDtoTrees;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

@Slf4j
public class Main {

    HashSet<Integer> booksProceeded = new HashSet<>();

    public static void main(String[] args) throws IOException {

        InputTaskDto inputTaskDto = FromFileToDtoTrees.getFromFile("c_incunabula.txt");
        final HashMap<Integer, Integer> booksRateScore = inputTaskDto.getBooksRateScore();
        int totalDaysLeft = inputTaskDto.getDaysToScan(); //1000

        List<Integer> resultLibList = new LinkedList<>();





        List<Library> libraries = inputTaskDto.getLibraries();
        while (totalDaysLeft > 0 && !libraries.isEmpty()) {


            BestLib bestLib = new BestLib();
            int iterationCount = 0;

            for (Library library : libraries) {
                if(iterationCount%100==0) {
                    log.info("IterCount {}", iterationCount);
                }
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

            if (bestLib.libId == -1) {
                break;
            }
            resultLibList.add(bestLib.libId);


            totalDaysLeft = totalDaysLeft - bestLib.signUpDay;
            libraries.removeIf(library -> library.getNumber() == bestLib.libId);


        }
        log.info("Here");
        System.out.println(resultLibList);


        FileReaderWriter.writeToFile(resultLibList);
    }

    static class BestLib {
        int libId = -1;
        long libScore;
        int signUpDay;
    }
}
