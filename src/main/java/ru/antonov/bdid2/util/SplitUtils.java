package ru.antonov.bdid2.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.OrderModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SplitUtils {
    private static final int COUNT_FILE = 20;
    private static final String PATH_TO_NEW_FILE = "/home/alexey/Downloads/selectPath/selectPart%s.csv";

    public static void createFiles(Path path) {
        int count = 0;
        int sizeFile = getSizeFile(path);
        int delimiter = (int) Math.ceil((double) sizeFile / COUNT_FILE);

        File file = path.toFile();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                FileWriter writer = new FileWriter(String.format(PATH_TO_NEW_FILE, count));
                for (int i = count * delimiter; i < (count + 1) * delimiter; i++) {
                    if (reader.ready()) {
                        writer.write(reader.readLine());
                        writer.write("\n");
                    } else {
                        break;
                    }
                }
                writer.close();
                count = count + 1;
            }
        } catch (IOException e) {
            log.info("Ошибка при разбиении файла " + count);
            e.printStackTrace();
        }
    }

    private static int getSizeFile(Path path) {
        int size = 0;
        try {
            size = (int) Files.lines(path, StandardCharsets.UTF_8).count();
        } catch (IOException e) {
            log.info("Ошибка при открытии файла");
            e.printStackTrace();
        }
        return size;
    }

    public static Map<String, List<OrderModel>> splitByRegion(List<OrderModel> orderModels) {
        return orderModels.stream()
            .filter(Objects::nonNull)
            .parallel()
            .collect(Collectors.groupingBy(OrderModel::getRegionId));
    }
}
