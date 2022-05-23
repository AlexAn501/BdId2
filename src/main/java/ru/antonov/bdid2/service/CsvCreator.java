package ru.antonov.bdid2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.SneakyThrows;
import ru.antonov.bdid2.dto.OrderModel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class CsvCreator {

    public static File createCsv(List<OrderModel> orderModels){
        byte[] orderForCsv = createByteArrayForCsvFormat(orderModels);
        return getZipFile(orderForCsv);
    }

    private static byte[] createByteArrayForCsvFormat(List<OrderModel> orderModels) {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

        CsvSchema schema = csvMapper.schemaFor(OrderModel.class)
            .withHeader()
            .withColumnSeparator(';');

        try {
            String res = csvMapper.writer(schema).writeValueAsString(orderModels);
            return res.getBytes("Windows-1251");
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException(
                String.format("Ошибка при выгрузке данных %s:%s", e.getClass().getSimpleName(), e.getMessage()));
        }
    }

    @SneakyThrows
    private static File getZipFile(byte[] csvFileInBytes) {
        Path tempFile = Files.createTempFile("temp", ".csv");
        File file = tempFile.toFile();

        try (BufferedOutputStream bfos = new BufferedOutputStream(new FileOutputStream(file))) {
            bfos.write(csvFileInBytes);
        } catch (IOException e) {
            throw new RuntimeException(
                String.format("Ошибка при выгрузке данных %s:%s", e.getClass().getSimpleName(), e.getMessage()));
        }
        return file;
    }
}

