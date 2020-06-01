package com.zsw.services;

import com.zsw.entitys.CrmObjectEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/31.
 */
public interface ICrmObjectService extends IBaseService{

    void newCrmObject(CrmObjectEntity crmObjectEntity, Integer currentUserId)throws Exception;

    CrmObjectEntity updateCrmObject(CrmObjectEntity crmObjectEntity, Integer currentUserId) throws Exception;

    CrmObjectEntity getCrmObject(CrmObjectEntity param) throws Exception;

    List<CrmObjectEntity> listCrmObjectEntity(Map<String, Object> paramMap) throws Exception;

    Integer listCrmObjectEntityCount(Map<String, Object> paramMap) throws Exception;

    String checkCrmObjectExist(CrmObjectEntity crmObjectEntity) throws Exception;

    void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception;
}
