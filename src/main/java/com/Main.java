package com;

import com.io.FileReaderWriter;
import com.utils.FromFileToDtoTrees;

import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        FromFileToDtoTrees.getFromFile("a_example.txt");

    }
}
