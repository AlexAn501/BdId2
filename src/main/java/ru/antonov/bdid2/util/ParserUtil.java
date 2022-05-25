package ru.antonov.bdid2.util;

import lombok.extern.slf4j.Slf4j;
import ru.antonov.bdid2.dto.OrderModel;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ParserUtil {
    private static final String SPLITERATOR = ",";

    private static final Function<String, OrderModel> PARSER_FROM_LINE_TO_ORDER_MODEL = new Function<String, OrderModel>() {
        @Override
        public OrderModel apply(String s) {
            List<String> fields = Arrays.stream(s.split(SPLITERATOR))
                .map(f -> f.replaceAll("\"", ""))
                .collect(Collectors.toList());
            return ParserUtil.parseToOrderModel(fields);
        }
    };

    public static List<OrderModel> getOrdersFromFile(Path pathToFile) {
        return FileUtil.getLinesFromCsvFile(pathToFile).stream()
            .peek(s -> System.out.println("перед парсиногом " + s))
            .map(PARSER_FROM_LINE_TO_ORDER_MODEL)
            .peek((s -> System.out.println("после парсинга " + s)))
            .collect(Collectors.toList());
    }

    public static OrderModel parseToOrderModel(List<String> fields) {
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
            .basisId(fields.get(11))
            .decisionTypeId(fields.get(12))
            .build();
    }
}
