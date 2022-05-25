package ru.antonov.bdid2.util.csvUtils;

import lombok.SneakyThrows;
import ru.antonov.bdid2.dto.OrderModel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class CsvCreatorInFileUtil extends AbstractCsvCreatorUtil {

    public static File createCsv(List<OrderModel> orderModels) {
        byte[] orderForCsv = createByteArrayForCsvFormat(orderModels);
        return getCsvFile(orderForCsv);
    }

    @SneakyThrows
    private static File getCsvFile(byte[] csvFileInBytes) {
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

