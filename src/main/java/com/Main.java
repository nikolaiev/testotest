package com;

import com.dto.InputTaskDto;
import com.dto.Library;
import com.io.FileReaderWriter;
import com.utils.FromFileToDtoTrees;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class Main {

    static HashSet<Integer> booksProceeded = new HashSet<>();


    public static void main(String[] args) throws IOException {

        InputTaskDto inputTaskDto = FromFileToDtoTrees.getFromFile("f_libraries_of_the_world.txt");
        final HashMap<Integer, Integer> booksRateScore = inputTaskDto.getBooksRateScore();
        int totalDaysLeft = inputTaskDto.getDaysToScan(); //1000

        List<BestLib> resultLibList = new LinkedList<>();

        List<Library> libraries = inputTaskDto.getLibraries();
        while (totalDaysLeft > 0 && !libraries.isEmpty()) {


            BestLib bestLib = new BestLib();

            System.out.println("Here Libs left real" + libraries.size());

            for (Library library : libraries) {
                final int bookPerDay = library.getBookPerDay();
                final int signUpDays = library.getSignUpDays();
                final int daysLeftAfteSignUp = totalDaysLeft - signUpDays;

                if (daysLeftAfteSignUp > 0) {
                    //process
                    int possibleBookToProcess = daysLeftAfteSignUp * bookPerDay;
                    if (possibleBookToProcess <= 0) {
                        possibleBookToProcess = library.getBooks().length - 1;
                    }
                    LinkedHashSet<Integer> libraryBooksIds =
                        IntStream.of(library.getBooks()).boxed().collect(Collectors.toCollection(LinkedHashSet::new));
                    libraryBooksIds.removeAll(booksProceeded);
                    if (possibleBookToProcess > libraryBooksIds.size()) {
                        possibleBookToProcess = libraryBooksIds.size();
                    }

                    List<Integer> booksIds = new ArrayList<>(libraryBooksIds).subList(0, possibleBookToProcess);
                    long totalRate = booksIds.stream().map(booksRateScore::get).mapToInt(Integer::intValue).sum();

                    //if (totalRate > bestLib.libScore || totalRate == bestLib.libScore && bestLib.signUpDay > signUpDays) //b_read_on
                    //if (bestLib.signUpDay >= signUpDays) //c_incunabula
                    //if (bestLib.signUpDay >= signUpDays && totalRate >= bestLib.libScore) //d_tought_choise
                    //if (bestLib.signUpDay >= signUpDays && totalRate >= bestLib.libScore) //f_libraries_of_the_world.txt
                    //if (bestLib.signUpDay >= signUpDays && totalRate >= bestLib.libScore) //e_so_many_books.txt

                    if (bestLib.signUpDay >= signUpDays && totalRate >= bestLib.libScore) {
                        bestLib.libId = library.getNumber();
                        bestLib.signUpDay = signUpDays;
                        bestLib.libScore = totalRate;
                        bestLib.booksSubmittedCount = booksIds.size();
                        bestLib.booksSubmittedList = booksIds.stream().mapToInt(Number::intValue).toArray();
                    }
                }
            }

            if (bestLib.libId == -1) {
                break;
            }

            booksProceeded.addAll(IntStream.of(InputTaskDto.libsMap.get(bestLib.libId).getBooks()).boxed()
                .collect(Collectors.toCollection(HashSet::new)));

            resultLibList.add(bestLib);

            totalDaysLeft = totalDaysLeft - bestLib.signUpDay;
            libraries.removeIf(library -> library.getNumber() == bestLib.libId);
            log.info("removedLib");
            log.info("Libs left {}", libraries.size());

        }
        log.info("Here");
        System.out.println(resultLibList);


        FileReaderWriter.writeToFile(resultLibList);
    }

    public static class BestLib {
        public int libId = -1;
        public long libScore = 0;
        public int signUpDay = Integer.MAX_VALUE;
        public long booksSubmittedCount;
        public int[] booksSubmittedList;
    }
}
