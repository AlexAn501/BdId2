package ru.antonov.bdid2.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileUtil {

    public static List<Long> getRegions(Path path) {
        try {
            return Files.lines(path, Charset.forName("WINDOWS-1251"))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong).collect(Collectors.toList());
        } catch (IOException e) {
            log.info("Ошибка при чтении файла");
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getLinesFromCsvFile(Path pathToFile) {
        try {
            return Files.lines(pathToFile, StandardCharsets.UTF_8)
                .skip(1)
                .collect(Collectors.toList());
        } catch (IOException e) {
            log.info("Ошибка при чтении из файла " + pathToFile.getFileName());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
