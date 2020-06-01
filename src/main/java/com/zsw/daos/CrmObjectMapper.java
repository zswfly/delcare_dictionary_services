package com.zsw.daos;

import com.zsw.entitys.CrmObjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface CrmObjectMapper {
    Integer listCrmObjectEntityCount(@Param("paramMap") Map<String,Object> paramMap);

    List<CrmObjectEntity> listCrmObjectEntity(@Param("paramMap") Map<String,Object> paramMap);

    void batchBan(@Param("paramMap")Map<String, Object> paramMap);
}