package ru.antonov.bdid2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.antonov.bdid2.dto.In;
import ru.antonov.bdid2.dto.OrderModel;
import ru.antonov.bdid2.util.ParserUtil;

import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class ArchiveCreatorService {

    public void execute(In in){
        String pathToCsv = in.getPathToCsv();
        List<OrderModel> ordersFromFile = ParserUtil.getOrdersFromFile(Paths.get(pathToCsv));


    }

}
