package com.zsw.services;

import com.zsw.entitys.ContainerEntity;
import com.zsw.entitys.user.UserDto;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/31.
 */
public interface IContainerService extends IBaseService{
    void newContainer(ContainerEntity containerEntity, Integer currentUserId)throws Exception;

    ContainerEntity updateContainer(ContainerEntity containerEntity, Integer currentUserId) throws Exception;

    ContainerEntity getContainer(ContainerEntity param) throws Exception;

    List<ContainerEntity> listContainerEntity(Map<String, Object> paramMap) throws Exception;

    Integer listContainerEntityCount(Map<String, Object> paramMap) throws Exception;

    String checkContainerExist(ContainerEntity containerEntity) throws Exception;

    void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception;

}
