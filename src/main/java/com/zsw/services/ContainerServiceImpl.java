package com.zsw.services;

import com.zsw.daos.ContainerMapper;
import com.zsw.entitys.ContainerEntity;
import com.zsw.entitys.user.UserDto;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.PinyinUtils;
import com.zsw.utils.UserServiceStaticWord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zhangshaowei on 2020/5/31.
 */
@Service
public class ContainerServiceImpl implements IContainerService,Serializable {
    private static final long serialVersionUID = 6820871758332119118L;

    @Autowired
    private IDBService dbService;

    @Resource
    private ContainerMapper containerMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void newContainer(ContainerEntity containerEntity, Integer currentUserId)throws Exception {
        containerEntity.setId(null);
        containerEntity.setMnemonicCode(PinyinUtils.getFirstSpell(containerEntity.getName()));
        containerEntity.setStatus(CommonStaticWord.Normal_Status_0);
        containerEntity.setCreateUser(currentUserId);
        containerEntity.setCreateTime(new Timestamp(new Date().getTime()));
        containerEntity.setUpdateUser(currentUserId);
        containerEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(containerEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public ContainerEntity updateContainer(ContainerEntity containerEntity, Integer currentUserId) throws Exception{
        if(containerEntity == null
                || containerEntity.getId() == null
                ){
            throw new Exception("参数错误");
        }

        ContainerEntity result = this.dbService.get(ContainerEntity.class,containerEntity.getId());

        if(result == null) throw new Exception("没有该用户id");

        BeanUtils.copyProperties(containerEntity,result);

        result.setUpdateUser(currentUserId);
        result.setUpdateTime(new Timestamp(new Date().getTime()));

        return result;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ContainerEntity getContainer(ContainerEntity param) throws Exception{
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ContainerEntity>  listContainerEntity(Map<String, Object> paramMap) throws Exception{
        return this.containerMapper.listContainerEntity(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer listContainerEntityCount(Map<String, Object> paramMap) throws Exception {
        return this.containerMapper.listContainerEntityCount(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public synchronized String checkContainerExist(ContainerEntity containerEntity) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        if(containerEntity.getId() == null){

            ContainerEntity param = new ContainerEntity();

            param.setName(containerEntity.getName());
            if( this.dbService.get(param) != null
                    ) stringBuilder.append("集装箱名已存在");

        }else{
            ContainerEntity param = new ContainerEntity();
            ContainerEntity result = null;

            param.setName(containerEntity.getName());
            result = this.dbService.get(param);
            if( result != null && result.getId() != containerEntity.getId() )
                stringBuilder.append("集装箱名已存在");

        }

        return stringBuilder.toString();
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception{
        int status = CommonStaticWord.Status_ban.equals(type)?CommonStaticWord.Ban_Status_1:CommonStaticWord.Normal_Status_0 ;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("currentUserId",currentUserId);
        paramMap.put("status",status);
        paramMap.put("ids",ids);
        this.containerMapper.batchBan(paramMap);

    }

}
