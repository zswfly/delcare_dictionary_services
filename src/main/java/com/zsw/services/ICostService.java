package com.zsw.services;

import com.zsw.entitys.CostEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/31.
 */
public interface ICostService extends IBaseService{
    void newCost(CostEntity costEntity, Integer currentUserId)throws Exception;

    CostEntity updateCost(CostEntity costEntity, Integer currentUserId) throws Exception;

    CostEntity getCost(CostEntity param) throws Exception;

    List<CostEntity> listCostEntity(Map<String, Object> paramMap) throws Exception;

    Integer listCostEntityCount(Map<String, Object> paramMap) throws Exception;

    String checkCostExist(CostEntity costEntity) throws Exception;

    void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception;



}
