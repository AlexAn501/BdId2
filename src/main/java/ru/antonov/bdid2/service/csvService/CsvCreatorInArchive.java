package ru.antonov.bdid2.service.csvService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.antonov.bdid2.dto.OrderModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class CsvCreatorInArchive extends AbstractCsvCreator {

    public static File createCsvArchive(Map<String, List<OrderModel>> orderModelsMap) {
        Map<String, byte[]> bytesOrderFromCsv = new HashMap<>();

        for (Map.Entry<String, List<OrderModel>> pair : orderModelsMap.entrySet()) {
            byte[] byteArrayForCsvFormat = createByteArrayForCsvFormat(pair.getValue());
            bytesOrderFromCsv.put(pair.getKey(), byteArrayForCsvFormat);
        }

        return getCsvZipFile(bytesOrderFromCsv);
    }


    @SneakyThrows
    private static File getCsvZipFile(Map<String, byte[]> bytesOrderFromCsv) {
        Path tempFile = Files.createTempFile("archive", ".zip");
        log.info("название архива " + tempFile.toString());
        File file = tempFile.toFile();

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))) {

            //записываем csv в архив
            for (Map.Entry<String, byte[]> pair : bytesOrderFromCsv.entrySet()) {
                String csvZipEntryName = String.format(pair.getKey() + ".csv", LocalTime.now());
                ZipEntry csvZipEntry = new ZipEntry(csvZipEntryName);
                zos.putNextEntry(csvZipEntry);
                zos.write(pair.getValue());
                zos.closeEntry();
            }

        } catch (IOException e) {
            throw new RuntimeException(
                String.format("Ошибка при выгрузке данных %s:%s", e.getClass().getSimpleName(), e.getMessage()));
        }
        return file;
    }
}
