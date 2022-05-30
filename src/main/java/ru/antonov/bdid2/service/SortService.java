package ru.antonov.bdid2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.In;
import ru.antonov.bdid2.dto.OrderModel;
import ru.antonov.bdid2.util.ParserUtil;
import ru.antonov.bdid2.util.SplitUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SortService {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void sort(In in) {
        List<String> pathToSortFile = in.getPathToSortFile();
        Path path = Paths.get(pathToSortFile.get(0));
        List<OrderModel> ordersFromFile = ParserUtil.getOrdersFromFile(path, Charset.forName("WINDOWS-1251"));
        Set<OrderModel> setOrders = ordersFromFile.parallelStream().collect(Collectors.toSet());
        List<OrderModel> sortedOrderModel = setOrders.stream().sorted().collect(Collectors.toList());

        byte[] byteArrayForCsvFormat = SplitUtils.createByteArrayForCsvFormat(sortedOrderModel, true);

        String addressFile = createOrAddInFile(path.getFileName().toString().substring(0, path.getFileName().toString().length() - 4),
            byteArrayForCsvFormat, path.getParent());
        log.info(String.format("файл записан %s", addressFile));



    }

    private String createOrAddInFile(String name, byte[] source, Path pathToDirectory) {
        Path path = Paths.get(pathToDirectory + "/sort" + File.separator + name + ".csv");
        try {
            if (Files.isRegularFile(path)) {
                try (BufferedOutputStream bfos = new BufferedOutputStream(new FileOutputStream(path.toFile(), true))) {
                    bfos.write(source);
                } catch (IOException e) {
                    throw new RuntimeException(
                        String.format("Ошибка при выгрузке данных %s:%s", e.getClass().getSimpleName(), e.getMessage()));
                }
            } else {
                Path pathToFile = Files.createFile(path);
                try (BufferedOutputStream bfos = new BufferedOutputStream(new FileOutputStream(pathToFile.toFile()))) {
                    bfos.write(source);
                } catch (IOException e) {
                    throw new RuntimeException(
                        String.format("Ошибка при выгрузке данных %s:%s", e.getClass().getSimpleName(), e.getMessage()));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(
                String.format("Ошибка при попытке создать файл %s:%s", e.getClass().getSimpleName(), e.getMessage()));
        }
        return path.toString();
    }
}
