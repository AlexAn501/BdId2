package ru.antonov.bdid2.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.Order;
import ru.antonov.bdid2.dto.OrderModel;
import ru.antonov.bdid2.mapper.MainMapper;
import ru.antonov.bdid2.nsi.NSIFeignClient;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FirstService {

    private final MainMapper mapper;

    private final NSIFeignClient nsi;

    public void execute() {
        List<Order> orders = mapper.getAllOrder();
        log.info(String.format("Запрос выполнен %s", orders.size()));

        List<OrderModel> orderModels = parseToOrderModel(orders);

        File csv = CsvCreator.createCsv(orderModels);
    }

    private List<OrderModel> parseToOrderModel(List<Order> orders) {
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
                }catch (FeignException e){
            log.info(String.format("битый сКУА МОДЭЛ %s", order));
        }
        return build;
        }).collect(Collectors.toList());
    }

    private String getNameOrEmptyString(Long id){
        return (id == null || id == -1) ?  "" : nsi.getCatalogRecordByRecordID(id).getName() ;
    }
}