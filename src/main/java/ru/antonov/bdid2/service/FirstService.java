package ru.antonov.bdid2.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.Order;
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

    public void execute(){
        List<Order> citizenshipIds = mapper.getAllOrder();
        log.info(String.format("Запрос выполнен %s", citizenshipIds.size()));

    }
}