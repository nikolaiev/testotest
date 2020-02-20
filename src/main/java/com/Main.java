package com;

import com.dto.InputTaskDto;
import com.io.FileReaderWriter;
import com.utils.FromFileToDtoTrees;

import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        final InputTaskDto inputTaskDto = FromFileToDtoTrees.getFromFile("a_example.txt");


    }
}
