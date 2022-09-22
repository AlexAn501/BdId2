package ru.antonov.bdid2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.In;
import ru.antonov.bdid2.dto.Order;
import ru.antonov.bdid2.dto.OrderModel;
import ru.antonov.bdid2.util.SplitUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArchiveCreatorService {
    public static final String PATH_TO_DIRECTORY =  "/home/alexey/Downloads/resultSelectFiles";
    public static final String PATH_TO_HUTS_ORDER = "/home/alexey/Downloads/demo";

    @Autowired
    private FirstService service;

    public void execute(In in) {
        log.info(String.format("%s принят в работу", in.getPathToCsv()));
        String pathToCsvString = in.getPathToCsv();

        Path path = Paths.get(pathToCsvString);
        Path pathToArchive = Paths.get(PATH_TO_DIRECTORY);

        List<Order> orders = getOrdersFromFile(path);
        List<OrderModel> orderModels = service.parseOrderListToOrderModelList(orders);
        orders = null;
        String hutsAddress = createHutsFile(pathToCsvString);
        log.info("Адрес битых идишников " + hutsAddress);

        Map<String, byte[]> regionByteOrder = SplitUtils.splitByRegionBytes(orderModels);

        regionByteOrder.entrySet().parallelStream().forEach(entry -> createOrAddInFile(entry.getKey(), entry.getValue(), pathToArchive));
    }

    private String createHutsFile(String name){
        String substring = name.substring(name.length() - 6, name.length() - 4);
        Path path = Paths.get(PATH_TO_HUTS_ORDER + File.separator + "huts" + substring +".txt");
        try {
            if(Files.isRegularFile(path)){
                FileWriter writer = new FileWriter(path.toFile(), true);
                for (Order hutsOrder : FirstService.hutsOrders) {
                    String toString = hutsOrder.toString();
                    writer.write(toString);
                }
            }else {
                Path filePath = Files.createFile(path);
                File file = filePath.toFile();
                FileWriter writer = new FileWriter(file, true);
                for (Order hutsOrder : FirstService.hutsOrders) {
                    String toString = hutsOrder.toString();
                    writer.write(toString);
                }
            }
        } catch (IOException e) {
            log.info("Ошибка при создании файла битых идишников");
            e.printStackTrace();
        }
        return path.toString();
    }

    private void createOrAddInFile(String name, byte[] source, Path pathToDirectory) {
        try {
            Path path = Paths.get(pathToDirectory + File.separator + name + ".csv");
            if (Files.isRegularFile(path)) {
                try (BufferedOutputStream bfos = new BufferedOutputStream(new FileOutputStream(path.toFile(), true))) {
                    bfos.write(source);
                } catch (IOException e) {
                    throw new RuntimeException(
                        String.format("Ошибка при выгрузке данных %s:%s", e.getClass().getSimpleName(), e.getMessage()));
                }
            } else {
                Path pathToFile = Files.createFile(Paths.get(pathToDirectory + File.separator + name + ".csv"));
                try (BufferedOutputStream bfos = new BufferedOutputStream(new FileOutputStream(pathToFile.toFile()))) {
                    bfos.write(source);
                } catch (IOException e) {
                    throw new RuntimeException(
                        String.format("Ошибка при выгрузке данных %s:%s", e.getClass().getSimpleName(), e.getMessage()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersFromFile(Path pathToFile) {
        return getLinesFromCsvFile(pathToFile).stream()
            .map(PARSER_FROM_LINE_TO_ORDER)
            .collect(Collectors.toList());
    }

    private final Function<String, Order> PARSER_FROM_LINE_TO_ORDER = new Function<String, Order>() {
        @Override
        public Order apply(String s) {
            List<String> fields = Arrays.stream(s.split(","))
                .map(f -> f.replaceAll("\"", ""))
                .collect(Collectors.toList());
            return getOrderFromString(fields);
        }
    };

    public List<String> getLinesFromCsvFile(Path pathToFile) {
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

    private Order getOrderFromString(List<String> fields) {
        try {
            Order build = Order.builder()
                .regionId(getIdOr0(fields.get(0)))
                .caseTypeId(getIdOr0(fields.get(1)))
                .applicationBasisId(getIdOr0(fields.get(2)))
                .lastName(fields.get(3))
                .firstname(fields.get(4))
                .middleName(fields.get(5))
                .genderId(getIdOr0(fields.get(6)))
                .birthCountryId(getIdOr0(fields.get(7)))
                .birthDate(LocalDate.parse(String.valueOf(fields.get(8))))
                .citizenshipId(getIdOr0(fields.get(9)))
                .decisionTypeId(getIdOr0(fields.get(10)))
                .basisId(getIdOr0(fields.get(11)))
                .decisionDate(LocalDate.parse(String.valueOf(fields.get(12))))
                .build();
            return build;
        }catch (Exception e){
            log.info(String.format("Ошибка парсинга %s", fields));
            throw new NumberFormatException(e.getMessage());
        }
    }

    private Long getIdOr0(String id) {
        try{
            return (id == null || id.isEmpty()) ? 0L : Long.parseLong(id);
        }catch (NumberFormatException e){
            log.info("ошибка парсинга " + id);
            throw new NumberFormatException(id);
        }
    }
}
