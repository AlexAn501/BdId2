package ru.antonov.bdid2.nsi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.antonov.bdid2.dto.RecordDto;

@FeignClient(url = "http://172.25.16.222:1234", name = "nsi")
public interface NSIFeignClient {

    @GetMapping("/search-nsi/public/v1/records/{recordId}")
     RecordDto getCatalogRecordByRecordID(@PathVariable("recordId") Long recordId);
}
