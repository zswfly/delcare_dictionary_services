package com.zsw.daos;

import com.zsw.entitys.CostEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface CostMapper {
    Integer listCostEntityCount(@Param("paramMap") Map<String,Object> paramMap);

    List<CostEntity> listCostEntity(@Param("paramMap") Map<String,Object> paramMap);

    void batchBan(@Param("paramMap")Map<String, Object> paramMap);
}