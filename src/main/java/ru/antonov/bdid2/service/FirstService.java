package ru.antonov.bdid2.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.Order;
import ru.antonov.bdid2.dto.OrderModel;
import ru.antonov.bdid2.dto.RecordDto;
import ru.antonov.bdid2.mapper.MainMapper;
import ru.antonov.bdid2.nsi.NSIFeignClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
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

        parseToOrderModel(orders);
    }

    private List<OrderModel> parseToOrderModel(List<Order> orders) {
        OrderModel orderModel = new OrderModel();

        Order order = orders.get(0);
        RecordDto region = nsi.getCatalogRecordByRecordID(order.getRegionId());
        RecordDto caseType = nsi.getCatalogRecordByRecordID(order.getCaseTypeId());
        RecordDto appliationBasis = nsi.getCatalogRecordByRecordID(order.getApplicationBasisId());
        RecordDto gender = nsi.getCatalogRecordByRecordID(order.getGenderId());
        RecordDto country = nsi.getCatalogRecordByRecordID(order.getBirthCountryId());
        RecordDto citizenship = nsi.getCatalogRecordByRecordID(order.getCitizenshipId());
        RecordDto decisionType = nsi.getCatalogRecordByRecordID(order.getDecisionTypeId());
        RecordDto basis = nsi.getCatalogRecordByRecordID(order.getBasisId());
        return null;
    }
}