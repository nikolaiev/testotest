package com.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileReaderWriter {

    public static List<String> readFile(String path) throws IOException {
        File file = new File(		FileReaderWriter.class.getClassLoader().getResource(path).getFile()
        );

        try {
            return FileUtils.readLines(file, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
