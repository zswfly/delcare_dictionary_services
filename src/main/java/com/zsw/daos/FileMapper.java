package com.zsw.daos;

import com.zsw.entitys.FileEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FileMapper {
    Integer listFileEntityCount(@Param("paramMap") Map<String,Object> paramMap);

    List<FileEntity> listFileEntity(@Param("paramMap") Map<String,Object> paramMap);

    void deleteFileByIds(@Param("ids") List<Integer> ids);

}