package ru.antonov.bdid2.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    public void checkPunchId(){
        List<Long> citizenshipIds = mapper.getCitizenshipIds();
        log.info("Запрос выполнен");

        List<Long> UnCorrectCitizenshipIds = citizenshipIds.parallelStream().filter(id -> {
            log.info(String.format("citizenshpId = %d", id));
            try {
                RecordDto catalogRecordByRecordID = nsi.getCatalogRecordByRecordID(id);
                String catalog = catalogRecordByRecordID.getCatalogCode();
                log.info(catalog);
                return catalog.equals("country");
            }catch (FeignException e){
                log.info(String.format("Некорректный citizenshpId = %d", id));
            }
            return false;
        }).collect(Collectors.toList());

        log.info(String.format("count UnCorrectCitizenshipIds = %d", UnCorrectCitizenshipIds.size()));
    }
}