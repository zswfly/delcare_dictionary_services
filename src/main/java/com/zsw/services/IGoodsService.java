package com.zsw.services;

import com.zsw.entitys.GoodsEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/31.
 */
public interface IGoodsService extends IBaseService{

    void newGoods(GoodsEntity goodsEntity, Integer currentUserId)throws Exception;

    GoodsEntity updateGoods(GoodsEntity goodsEntity, Integer currentUserId) throws Exception;

    GoodsEntity getGoods(GoodsEntity param) throws Exception;

    List<GoodsEntity> listGoodsEntity(Map<String, Object> paramMap) throws Exception;

    Integer listGoodsEntityCount(Map<String, Object> paramMap) throws Exception;

    String checkGoodsExist(GoodsEntity goodsEntity) throws Exception;

    void batchBan(List<Integer> ids, String type, Integer currentUserId) throws Exception;



}
