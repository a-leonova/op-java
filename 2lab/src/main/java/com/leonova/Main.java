package com.leonova;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.io.File;

public class Main {
    private static final String HELP_STRING = "[--name fileName] [--data searchString] <folder|file>";
    private static String searchString;
    private static File startFile;
    private static Predicate<File> fileFilter = f -> true;

    public static void main(String[] args) {
        try{
            getArgs(args);
            FileSearcher fileSearcher = new FileSearcher();

            List<File> filteredFiles = fileSearcher.searchFiles(startFile, fileFilter);

            if(searchString != null){
                List<DataGetter> dataGetters = new ArrayList<>(filteredFiles.size());

                for(File file : filteredFiles){
                    DataGetter dataGetter = new DataGetter(file, searchString);
                    dataGetters.add(dataGetter);
                    dataGetter.run();
                }
                System.out.println("String was found in files: ");
                dataGetters.stream()
                        .filter(DataGetter::isFoundString)
                        .forEach(dg -> System.out.println(dg.getFile()));
            } else {
                System.out.println("Found files: ");
                filteredFiles.forEach(System.out::println);
            }
        } catch (IllegalArgumentException | NullPointerException e){
            System.out.println(e.getMessage());
        }
    }

    private static void getArgs(String[] args){
        if(args.length < 1){
            throw new IllegalArgumentException("No enough args. Use: " + HELP_STRING);
        }

        for(int i = 0; i < args.length; ++i){
            switch (args[i]){
                case "--name":
                    if(i + 1 >= args.length){
                        throw new IllegalArgumentException("No enough args. Use: " + HELP_STRING);
                    }
                    String fileName = args[i + 1];
                    fileFilter = f -> f.getName().equals(fileName);
                    ++i;
                    break;
                case "--data":
                    if(i + 1 >= args.length){
                        throw new IllegalArgumentException("No enough args. Use: " + HELP_STRING);
                    }
                    searchString = args[i + 1];
                    ++i;
                    break;
                case "-h":
                    System.out.println(HELP_STRING);
                    System.exit(0);
                    break;
                default:
                    startFile = Paths.get(args[i]).toFile();
            }
        }
        if(startFile == null){
            throw new IllegalArgumentException("There must be folder|file for start searching. Use: " + HELP_STRING);
        }

    }
}
