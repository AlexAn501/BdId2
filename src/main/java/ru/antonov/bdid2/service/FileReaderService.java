package ru.antonov.bdid2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.OrderModel;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileReaderService {
    public Stream<OrderModel> getOrdersFromFile(Path pathToFile) {
        Stream<OrderModel> orderModels = null;
        try {
            Function<String, OrderModel> parser = new Function<String, OrderModel>() {
                @Override
                public OrderModel apply(String s) {
                    List<String> fields = Arrays.stream(s.split(";"))
                        .map(f -> f.replaceAll("\"", ""))
                        .collect(Collectors.toList());

                    return OrderModel.builder()
                        .regionId(fields.get(0))
                        .caseTypeId(fields.get(1))
                        .applicationBasisId(fields.get(2))
                        .decisionDate(String.valueOf(fields.get(3)))
                        .lastName(fields.get(4))
                        .firstname(fields.get(5))
                        .middleName(fields.get(6))
                        .birthDate(String.valueOf(fields.get(7)))
                        .genderId(fields.get(8))
                        .citizenshipId(fields.get(9))
                        .birthCountryId(fields.get(10))
                        .decisionTypeId(fields.get(11))
                        .basisId(fields.get(12))
                        .build();
                }
            };

            orderModels = Files.lines(pathToFile, Charset.forName("WINDOWS-1251"))
                .skip(1)
                .map(parser);
        } catch (IOException e) {
            log.info("Ошибка при чтении файла");
            e.printStackTrace();
        }
        return orderModels;
    }
}
