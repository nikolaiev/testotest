package com;

import com.dto.InputTaskDto;
import com.dto.Library;
import com.io.FileReaderWriter;
import com.utils.FromFileToDtoTrees;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
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
            int iterationCount = 0;

            System.out.println("Here Libs left real" + libraries.size());

            for (Library library : libraries) {
                if (iterationCount % 100 == 0) {
                    log.info("IterCount {}", iterationCount);
                }
                final int bookPerDay = library.getBookPerDay();
                final int signUpDays = library.getSignUpDays();
                final int daysLeftASfteSignUp = totalDaysLeft - signUpDays;
                if (daysLeftASfteSignUp <= 0) {

                } else {
                    //process
                    log.info("point1");
                    int possibleBookToProcess = daysLeftASfteSignUp * bookPerDay;
                    if (possibleBookToProcess <= 0) {
                        possibleBookToProcess = library.getBooks().length - 1;
                    }
                    if(possibleBookToProcess>library.getBooks().length-1){
                        possibleBookToProcess = library.getBooks().length-1;
                    }

                    final int[] books = new int[possibleBookToProcess];
                    System.arraycopy(library.getBooks(), 0, books, 0, possibleBookToProcess);
                    LinkedHashSet<Integer> collect = IntStream.of(books).boxed().collect(Collectors.toCollection(LinkedHashSet::new));
                    log.info("point2");
                    collect.removeAll(booksProceeded);
                    final int[] bookWillBeProceeded = collect.stream().mapToInt(Number::intValue).toArray();

                    final long totalRate = Arrays.stream(bookWillBeProceeded).mapToLong(booksRateScore::get).sum();

                    if (totalRate > bestLib.libScore) {
                        bestLib.libId = library.getNumber();
                        bestLib.signUpDay = library.getSignUpDays();
                        bestLib.libScore = totalRate;
                        bestLib.booksSubmittedCount = possibleBookToProcess;
                        bestLib.booksSubmittedList = books;
                    }
                }

                iterationCount++;
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
        public long libScore;
        public int signUpDay;
        public int booksSubmittedCount;
        public int [] booksSubmittedList;
    }
}
