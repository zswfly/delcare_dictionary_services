package com.zsw.daos;

import com.zsw.entitys.GoodsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface GoodsMapper {
    Integer listGoodsEntityCount(@Param("paramMap") Map<String,Object> paramMap);

    List<GoodsEntity> listGoodsEntity(@Param("paramMap") Map<String,Object> paramMap);

    void batchBan(@Param("paramMap")Map<String, Object> paramMap);
}