package ru.antonov.bdid2.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.Order;
import ru.antonov.bdid2.dto.OrderModel;
import ru.antonov.bdid2.mapper.MainMapper;
import ru.antonov.bdid2.nsi.NSIFeignClient;
import ru.antonov.bdid2.service.csvService.CsvCreatorInArchive;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class FirstService {

    private final FileReaderService reader;
    private final MainMapper mapper;
    private final NSIFeignClient nsi;

    public void executeFromFile(){
        String path = "/home/alexey/Рабочий стол/г. Санкт-Петербург.csv";

        Stream<OrderModel> ordersFromFile = reader.getOrdersFromFile(Paths.get(path));
        Map<String, List<OrderModel>> stringListMap = splitByRegion(ordersFromFile);
        File csvArchive = CsvCreatorInArchive.createCsvArchive(stringListMap);
    }

    public void executeFromBd() {
        List<Order> orders = mapper.getAllOrderV2();
        log.info(String.format("Запрос выполнен %s", orders.size()));

        Stream<OrderModel> orderModels = parseToOrderModel(orders);
        Map<String, List<OrderModel>> stringListMap = splitByRegion(orderModels);

        File archive = CsvCreatorInArchive.createCsvArchive(stringListMap);
    }

    private Map<String, List<OrderModel>> splitByRegion(Stream<OrderModel> orderModels) {
        return orderModels.filter(Objects::nonNull).parallel().collect(Collectors.groupingBy(OrderModel::getRegionId));
    }

    private Stream<OrderModel> parseToOrderModel(List<Order> orders) {
        return orders.parallelStream().map(order -> {
            OrderModel build = null;
            try {
                build = OrderModel.builder()
                    .regionId(getNameOrEmptyString(order.getRegionId()))
                    .caseTypeId(getNameOrEmptyString(order.getCaseTypeId()))
                    .applicationBasisId(getNameOrEmptyString(order.getApplicationBasisId()))
                    .genderId(getNameOrEmptyString(order.getGenderId()))
                    .birthCountryId(getNameOrEmptyString(order.getBirthCountryId()))
                    .citizenshipId(getNameOrEmptyString(order.getCitizenshipId()))
                    .decisionTypeId(getNameOrEmptyString(order.getDecisionTypeId()))
                    .basisId(getNameOrEmptyString(order.getBasisId()))

                    .lastName(order.getLastName())
                    .firstname(order.getFirstname())
                    .middleName(order.getMiddleName())
                    .birthDate(String.valueOf(order.getBirthDate()))
                    .decisionDate(String.valueOf(order.getDecisionDate()))
                    .build();
            } catch (FeignException e) {
                log.info(String.format("битый сКУА МОДЭЛ %s", order));
            }
            return build;
        });
    }

    private String getNameOrEmptyString(Long id) {
        return (id == null || id == -1) ? "" : nsi.getCatalogRecordByRecordID(id).getName();
    }
}