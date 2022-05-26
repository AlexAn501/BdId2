package ru.antonov.bdid2.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import ru.antonov.bdid2.dto.OrderModel;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

//    public static Map<String, byte[]> createCsvArchive(Map<String, List<OrderModel>> orderModelsMap) {
//        Map<String, byte[]> bytesOrderFromCsv = new HashMap<>();
//
//        for (Map.Entry<String, List<OrderModel>> pair : orderModelsMap.entrySet()) {
//            byte[] byteArrayForCsvFormat = createByteArrayForCsvFormat(pair.getValue());
//            bytesOrderFromCsv.put(pair.getKey(), byteArrayForCsvFormat);
//        }
//        return bytesOrderFromCsv;
//    }
//
//    private static byte[] createByteArrayForCsvFormat(List<OrderModel> orderModels) {
//        CsvMapper csvMapper = new CsvMapper();
//        csvMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
//
//        CsvSchema schema = csvMapper.schemaFor(OrderModel.class)
//            .withHeader()
//            .withColumnSeparator(';');
//
//        try {
//            String res = csvMapper.writer(schema).writeValueAsString(orderModels);
//            return res.getBytes("Windows-1251");
//        } catch (JsonProcessingException | UnsupportedEncodingException e) {
//            throw new RuntimeException(
//                String.format("Ошибка при выгрузке данных %s:%s", e.getClass().getSimpleName(), e.getMessage()));
//        }
//    }
}
