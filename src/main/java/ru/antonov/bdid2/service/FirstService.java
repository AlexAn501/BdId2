package ru.antonov.bdid2.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.In;
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

@Service
@AllArgsConstructor
@Slf4j
public class FirstService {

    private final MainMapper mapper;
    private final NSIFeignClient nsi;

    public String executeFromBdByRegion(In pathToRegion) {
        String pathRegions = pathToRegion.getPath();
        List<Long> regions = FileReaderService.getRegions(Paths.get(pathRegions));

        List<OrderModel> orders = Objects.requireNonNull(regions).parallelStream()
            .map(mapper::getAllOrderByRegion)
            .map(this::parseOrderListToOrderModel)
            .flatMap(List::stream).collect(Collectors.toList());

        Map<String, List<OrderModel>> stringListMap = splitByRegion(orders);
        File csvArchive = CsvCreatorInArchive.createCsvArchive(stringListMap);
        return csvArchive.getAbsolutePath();
    }

    public void executeFromFile() {
        String path = "/home/alexey/Рабочий стол/г. Санкт-Петербург.csv";

        List<OrderModel> ordersFromFile = FileReaderService.getOrdersFromFile(Paths.get(path));
        Map<String, List<OrderModel>> stringListMap = splitByRegion(ordersFromFile);
        File csvArchive = CsvCreatorInArchive.createCsvArchive(stringListMap);
    }

    public void executeFromBd() {
        List<Order> orders = mapper.getAllOrderV2();
        log.info(String.format("Запрос выполнен %s", orders.size()));

        List<OrderModel> orderModels = parseOrderListToOrderModel(orders);
        Map<String, List<OrderModel>> stringListMap = splitByRegion(orderModels);

        File archive = CsvCreatorInArchive.createCsvArchive(stringListMap);
    }

    private Map<String, List<OrderModel>> splitByRegion(List<OrderModel> orderModels) {
        return orderModels.stream().filter(Objects::nonNull).parallel().collect(Collectors.groupingBy(OrderModel::getRegionId));
    }

    private List<OrderModel> parseOrderListToOrderModel(List<Order> orders) {
        return orders.parallelStream()
            .filter(Objects::nonNull)
            .map(order -> {
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
        }).collect(Collectors.toList());
    }

    private String getNameOrEmptyString(Long id) {
        return (id == null || id == -1) ? "" : nsi.getCatalogRecordByRecordID(id).getName();
    }
}