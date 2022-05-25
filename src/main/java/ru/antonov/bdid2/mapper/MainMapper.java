package ru.antonov.bdid2.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ru.antonov.bdid2.dto.Order;

import java.util.List;

@Mapper
public interface MainMapper {

    List<Order> getAllOrder();

    List<Order> getAllOrderV2();

    List<Order> getAllOrderByRegion(@Param("regionId") Long regionId);
}
