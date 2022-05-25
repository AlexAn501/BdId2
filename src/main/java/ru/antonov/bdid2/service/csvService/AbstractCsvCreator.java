package ru.antonov.bdid2.service.csvService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ru.antonov.bdid2.dto.OrderModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class AbstractCsvCreator {

    protected static byte[] createByteArrayForCsvFormat(List<OrderModel> orderModels) {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

        CsvSchema schema = csvMapper.schemaFor(OrderModel.class)
            .withHeader()
            .withColumnSeparator(';');
        try {
            String res = csvMapper.writer(schema).writeValueAsString(orderModels);
            return res.getBytes("Windows-1251");
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException(
                String.format("Ошибка при выгрузке данных %s:%s", e.getClass().getSimpleName(), e.getMessage()));
        }
    }
}
