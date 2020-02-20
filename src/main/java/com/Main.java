package com;

import com.dto.InputTaskDto;
import com.dto.Library;
import com.io.FileReaderWriter;
import com.utils.FromFileToDtoTrees;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

@Slf4j
public class Main {

    static  HashSet<Integer> booksProceeded = new HashSet<>();


    public static void main(String[] args) throws IOException {

        InputTaskDto inputTaskDto = FromFileToDtoTrees.getFromFile("c_incunabula.txt");
        final HashMap<Integer, Integer> booksRateScore = inputTaskDto.getBooksRateScore();
        int totalDaysLeft = inputTaskDto.getDaysToScan(); //1000

        List<Integer> resultLibList = new LinkedList<>();

        List<Library> libraries = inputTaskDto.getLibraries();
        while (totalDaysLeft > 0 && !libraries.isEmpty()) {


            BestLib bestLib = new BestLib();
            int iterationCount = 0;

            System.out.println("Here Libs left real"+ libraries.size());

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
                    int possibleBookToProcess = daysLeftASfteSignUp * bookPerDay;
                    LinkedHashSet<Integer> collect = IntStream.of(library.getBooks()).boxed().collect( Collectors.toCollection( LinkedHashSet::new ) );
                    collect.removeAll(booksProceeded);
                        final int[] bookWillBeProceeded = collect.stream().mapToInt(Number::intValue).toArray();

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

            booksProceeded.addAll(IntStream.of(InputTaskDto.libsMap.get(bestLib.libId).getBooks()).boxed()
                .collect(Collectors.toCollection(HashSet::new)));

                resultLibList.add(bestLib.libId);

            totalDaysLeft = totalDaysLeft - bestLib.signUpDay;
            libraries.removeIf(library -> library.getNumber() == bestLib.libId);
            log.info("removedLib");
            log.info("Libs left {}", libraries.size());

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
