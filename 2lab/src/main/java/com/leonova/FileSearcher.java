package com.leonova;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSearcher {
    public List<File> searchFiles(File startFile, Predicate<File> filter){
        if (!startFile.exists()) {
            throw new IllegalArgumentException("folder|file does not exist");
        }

        if(startFile.isFile()){
            if(filter.test(startFile)){
                return List.of(startFile);
            }
            return List.of();
        }

        return Stream.of(Objects.requireNonNull(startFile.listFiles()))
                .flatMap(f -> searchFiles(f, filter).stream())
                .collect(Collectors.toList());
    }
}
