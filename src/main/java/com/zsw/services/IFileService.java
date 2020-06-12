package com.zsw.services;

import com.zsw.entitys.FileEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/31.
 */
public interface IFileService extends IBaseService{
    void newFile(FileEntity fileEntity, Integer currentUserId)throws Exception;


    FileEntity getFile(FileEntity param) throws Exception;

    List<FileEntity> listFileEntity(Map<String, Object> paramMap) throws Exception;

    Integer listFileEntityCount(Map<String, Object> paramMap) throws Exception;

    void deleteFileByIds(List<Integer> ids) throws Exception;

}
