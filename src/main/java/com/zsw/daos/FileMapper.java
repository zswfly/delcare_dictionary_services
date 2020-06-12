package com.zsw.daos;

import com.zsw.entitys.FileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface FileMapper {
    Integer listFileEntityCount(@Param("paramMap") Map<String,Object> paramMap);

    List<FileEntity> listFileEntity(@Param("paramMap") Map<String,Object> paramMap);

    void deleteFileByIds(@Param("ids") List<Integer> ids);

}