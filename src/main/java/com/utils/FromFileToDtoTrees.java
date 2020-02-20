package com.utils;

import com.dto.InputTaskDto;
import com.dto.Library;
import com.io.FileReaderWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FromFileToDtoTrees {



    public
    static InputTaskDto getFromFile(String name) throws IOException {
        HashMap<Integer, Integer> books = new HashMap();
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
            books.put(i, Integer.valueOf(split1[i]));
        }
        result.setBooksRateScore(books);


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

            final int[] objects = Arrays.stream(inputFileLines.get(libDescLineNum + 1).split("\\s"))
                .mapToInt(i -> Integer.valueOf(i)).toArray();//

            library.setBooks(objects);
            library.setNumber(libCounter);
            listLibs.add(library);
            libDescLineNum += 2;
            libCounter++;


        }
        result.setLibrariesCount(listLibs.size());
        result.setLibraries(listLibs);
        return result;

    }
}
