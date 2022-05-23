package ru.antonov.bdid2.mapper;

import org.apache.ibatis.annotations.Mapper;
import ru.antonov.bdid2.dto.Order;

import java.util.List;

@Mapper
public interface MainMapper {

    List<Order> getAllOrder();
}
