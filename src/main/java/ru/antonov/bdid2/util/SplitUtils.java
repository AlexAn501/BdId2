package ru.antonov.bdid2.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.OrderModel;
import ru.antonov.bdid2.service.ArchiveCreatorService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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

    public static Map<String, byte[]> splitByRegionBytes(List<OrderModel> orderModels) {
        return replaceListWithBytes(orderModels.stream()
            .filter(Objects::nonNull)
            .parallel()
            .sorted()
            .collect(Collectors.groupingBy(OrderModel::getRegionId)));
    }

    public static Map<String, byte[]> replaceListWithBytes(Map<String, List<OrderModel>> orderModelsMap) {
        Map<String, byte[]> bytesOrderFromCsv = new HashMap<>();

        for (Map.Entry<String, List<OrderModel>> pair : orderModelsMap.entrySet()) {
            Boolean isWithHeaders = checkHeader(pair.getKey());
            byte[] byteArrayForCsvFormat = createByteArrayForCsvFormat(pair.getValue(), isWithHeaders);
            bytesOrderFromCsv.put(pair.getKey(), byteArrayForCsvFormat);
        }
        return bytesOrderFromCsv;
    }

    private static Boolean checkHeader(String name) {
        Path path = Paths.get(ArchiveCreatorService.PATH_TO_DIRECTORY + File.separator + name + ".csv");
        return !Files.isRegularFile(path);
    }

    public static byte[] createByteArrayForCsvFormat(List<OrderModel> orderModels,@NotNull Boolean isWithHeader) {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        CsvSchema schema = null;

        if(isWithHeader) {
            schema = csvMapper.schemaFor(OrderModel.class)
                .withHeader()
                .withColumnSeparator(';');
        }else {
            schema = csvMapper.schemaFor(OrderModel.class)
                .withColumnSeparator(';');
        }

        try {
            String res = csvMapper.writer(schema).writeValueAsString(orderModels);
            return res.getBytes("Windows-1251");
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException(
                String.format("Ошибка при выгрузке данных %s:%s", e.getClass().getSimpleName(), e.getMessage()));
        }
    }
}
