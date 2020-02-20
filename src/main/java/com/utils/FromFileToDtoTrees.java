package com.utils;

import com.dto.InputTaskDto;
import com.dto.Library;
import com.io.FileReaderWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FromFileToDtoTrees {

    public
    static InputTaskDto getFromFile(String name) throws IOException {
        HashMap<Integer, Integer> booksRates = new HashMap();
        InputTaskDto result = new InputTaskDto();
        final List<String> inputFileLines = FileReaderWriter.readFile(name);

        final String meta = inputFileLines.get(0);
        final String[] split = meta.split("\\s");
        System.out.println(split);
        result.setBookCountTotal(Integer.valueOf(split[0]));
        final Integer librariesCountTotal = Integer.valueOf(split[1]);
        result.setLibrariesCount(librariesCountTotal);
        result.setDaysToScan(Integer.valueOf(split[2]));

        final String ratesBooks = inputFileLines.get(1);
        final String[] split1 = ratesBooks.split("\\s");
        for (int i = 0; i < split1.length; i++) {
            booksRates.put(i, Integer.valueOf(split1[i]));
        }
        result.setBooksRateScore(booksRates);


        //libs
        int libDescLineNum = 2;
        int libCounter = 0;
        List<Library> listLibs = new ArrayList<>();
        while (libCounter < librariesCountTotal) {

            Library library = new Library();
            final String[] split2 = inputFileLines.get(libDescLineNum).split("\\s");//
            final Integer booksCount = Integer.valueOf(split2[0]);
            library.setBooksCount(booksCount);
            library.setSignUpDays(Integer.valueOf(split2[1]));
            library.setBookPerDay(Integer.valueOf(split2[2]));

            final String[] bookRatesString = inputFileLines.get(libDescLineNum + 1).split("\\s");
            //take bookId
            final int[] sortedBookIds = Arrays.stream(bookRatesString)
                .sorted(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return booksRates.get(Integer.valueOf(o2)) - booksRates.get(Integer.valueOf(o1));
                    }
                })
                    .mapToInt(Integer::valueOf).toArray();

            library.setBooks(sortedBookIds);
            library.setNumber(libCounter);
            listLibs.add(library);
            InputTaskDto.libsMap.put(library.getNumber(),library);
            libDescLineNum += 2;
            libCounter++;


        }
        result.setLibrariesCount(listLibs.size());
        result.setLibraries(listLibs);
        return result;

    }
}
