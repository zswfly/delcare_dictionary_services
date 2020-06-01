package com.zsw.services;

import com.zsw.daos.GoodsMapper;
import com.zsw.entitys.GoodsEntity;
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
public class GoodsServiceImpl implements IGoodsService,Serializable {

    private static final long serialVersionUID = -4390300009643383975L;


    @Autowired
    private IDBService dbService;

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void newGoods(GoodsEntity goodsEntity, Integer currentUserId)throws Exception {
        goodsEntity.setId(null);
        goodsEntity.setMnemonicCode(PinyinUtils.getFirstSpell(goodsEntity.getName()));
        goodsEntity.setStatus(CommonStaticWord.Normal_Status_0);
        goodsEntity.setCreateUser(currentUserId);
        goodsEntity.setCreateTime(new Timestamp(new Date().getTime()));
        goodsEntity.setUpdateUser(currentUserId);
        goodsEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(goodsEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public GoodsEntity updateGoods(GoodsEntity goodsEntity, Integer currentUserId) throws Exception{
        if(goodsEntity == null
                || goodsEntity.getId() == null
                ){
            throw new Exception("参数错误");
        }

        GoodsEntity result = this.dbService.get(GoodsEntity.class,goodsEntity.getId());

        if(result == null) throw new Exception("没有该用户id");

        BeanUtils.copyProperties(goodsEntity,result);

        result.setUpdateUser(currentUserId);
        result.setUpdateTime(new Timestamp(new Date().getTime()));

        return result;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public GoodsEntity getGoods(GoodsEntity param) throws Exception{
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GoodsEntity> listGoodsEntity(Map<String, Object> paramMap) throws Exception{
        return this.goodsMapper.listGoodsEntity(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer listGoodsEntityCount(Map<String, Object> paramMap) throws Exception {
        return this.goodsMapper.listGoodsEntityCount(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public synchronized String checkGoodsExist(GoodsEntity goodsEntity) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        if(goodsEntity.getId() == null){

            GoodsEntity param = new GoodsEntity();

            param.setName(goodsEntity.getName());
            if( this.dbService.get(param) != null
                    ) stringBuilder.append("集装箱名已存在");

        }else{
            GoodsEntity param = new GoodsEntity();
            GoodsEntity result = null;

            param.setName(goodsEntity.getName());
            result = this.dbService.get(param);
            if( result != null && result.getId() != goodsEntity.getId() )
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
        this.goodsMapper.batchBan(paramMap);

    }
}
