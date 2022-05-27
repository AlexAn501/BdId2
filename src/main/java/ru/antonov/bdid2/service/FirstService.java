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
import ru.antonov.bdid2.util.csvUtils.CsvCreatorInArchiveUtil;
import ru.antonov.bdid2.util.ParserUtil;
import ru.antonov.bdid2.util.SplitUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FirstService {

    private final MainMapper mapper;
    private final NSIFeignClient nsi;
    public static List<Order> hutsOrders = new CopyOnWriteArrayList<>();
//
//    public String executeFromBdByRegion(In pathToRegion) {
//        String pathRegions = pathToRegion.getPathRegion();
//        List<Long> regions = FileReaderService.getRegions(Paths.get(pathRegions));
//
//        List<OrderModel> orders = Objects.requireNonNull(regions).parallelStream()
//            .map(mapper::getAllOrderByRegion)
//            .map(this::parseOrderListToOrderModel)
//            .flatMap(List::stream).collect(Collectors.toList());
//
//        Map<String, List<OrderModel>> stringListMap = splitByRegion(orders);
//        File csvArchive = CsvCreatorInArchive.createCsvArchive(stringListMap);
//        return csvArchive.getAbsolutePath();
//    }

    public String executeFromFile(In in) {
        String pathCsv = in.getPathToCsv();
        List<OrderModel> ordersFromFile = ParserUtil.getOrdersFromFile(Paths.get(pathCsv));
        Map<String, List<OrderModel>> stringListMap = SplitUtils.splitByRegion(ordersFromFile);
        File csvArchive = CsvCreatorInArchiveUtil.createCsvArchive(stringListMap);
        return csvArchive.getAbsolutePath();
    }

    public void executeFromBd() {
        List<Order> orders = mapper.getAllOrderV2();
        log.info(String.format("Запрос выполнен %s", orders.size()));

        List<OrderModel> orderModels = parseOrderListToOrderModelList(orders);
        Map<String, List<OrderModel>> stringListMap = SplitUtils.splitByRegion(orderModels);

        File archive = CsvCreatorInArchiveUtil.createCsvArchive(stringListMap);
    }

    public List<OrderModel> parseOrderListToOrderModelList(List<Order> orders) {
        return orders.stream()
            .filter(Objects::nonNull)
            .parallel()
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
                    log.info(String.format("битый сУКА МОДЭЛ %s", order));
                    hutsOrders.add(order);
                }
                return build;
            }).collect(Collectors.toList());
    }

    private String getNameOrEmptyString(Long id) {
        return (id == null || id == 0 || id == -1) ? "" : nsi.getCatalogRecordByRecordID(id).getName();
    }
}