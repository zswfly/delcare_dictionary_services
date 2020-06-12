package com.zsw.services;

import com.zsw.daos.FileMapper;
import com.zsw.entitys.FileEntity;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.PinyinUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/31.
 */
@Service
public class FileServiceImpl implements IFileService,Serializable {

    private static final long serialVersionUID = 3476896975498664639L;

    @Autowired
    private IDBService dbService;

    @Resource
    private FileMapper fileMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void newFile(FileEntity fileEntity, Integer currentUserId)throws Exception {
        //todo  服务商文件新增


        fileEntity.setId(null);
        fileEntity.setCreateUser(currentUserId);
        fileEntity.setCreateTime(new Timestamp(new Date().getTime()));
        fileEntity.setUpdateUser(currentUserId);
        fileEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(fileEntity);


    }



    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public FileEntity getFile(FileEntity param) throws Exception{
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<FileEntity>  listFileEntity(Map<String, Object> paramMap) throws Exception{
        return this.fileMapper.listFileEntity(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer listFileEntityCount(Map<String, Object> paramMap) throws Exception {
        return this.fileMapper.listFileEntityCount(paramMap);
    }



    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void deleteFileByIds(List<Integer> ids) throws Exception{
        this.fileMapper.deleteFileByIds(ids);
        //todo  服务商文件删除
    }

}
