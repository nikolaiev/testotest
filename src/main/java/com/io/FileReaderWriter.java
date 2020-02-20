package com.io;

import com.Main;
import com.dto.InputTaskDto;
import com.dto.Library;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class FileReaderWriter {

    public static List<String> readFile(String path) throws IOException {
        File file = new File(FileReaderWriter.class.getClassLoader().getResource(path).getFile()
        );

        try {
            return FileUtils.readLines(file, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void writeToFile(List<Main.BestLib> resultLibList) {
        File file = new File("output.txt");
        StringBuilder result = new StringBuilder();
        result.append(resultLibList.size())
                .append(System.lineSeparator());
        for (Main.BestLib bestLib : resultLibList) {
            int libraryId =bestLib.libId;
            Library library = InputTaskDto.libsMap.get(libraryId);
            result.append(libraryId)
                    .append(" ")
                    .append(bestLib.booksSubmittedList.length)
                    .append(System.lineSeparator());
            result.append(Arrays.toString(bestLib.booksSubmittedList).replaceAll("[\\]\\[\\,]", ""))
            .append(System.lineSeparator());
        }
        try {
            FileUtils.writeStringToFile(file, result.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
