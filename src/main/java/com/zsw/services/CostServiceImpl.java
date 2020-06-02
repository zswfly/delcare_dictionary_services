package com.zsw.services;

import com.zsw.daos.CostMapper;
import com.zsw.entitys.CostEntity;
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
public class CostServiceImpl implements ICostService,Serializable {

    private static final long serialVersionUID = 2816362909126489205L;

    @Autowired
    private IDBService dbService;

    @Resource
    private CostMapper costMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void newCost(CostEntity costEntity, Integer currentUserId)throws Exception {
        costEntity.setId(null);
        costEntity.setMnemonicCode(PinyinUtils.getFirstSpell(costEntity.getName()));
        costEntity.setStatus(CommonStaticWord.Normal_Status_0);
        costEntity.setCreateUser(currentUserId);
        costEntity.setCreateTime(new Timestamp(new Date().getTime()));
        costEntity.setUpdateUser(currentUserId);
        costEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(costEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public CostEntity updateCost(CostEntity costEntity, Integer currentUserId) throws Exception{
        if(costEntity == null
                || costEntity.getId() == null
                ){
            throw new Exception("参数错误");
        }

        CostEntity result = this.dbService.get(CostEntity.class,costEntity.getId());

        if(result == null) throw new Exception("没有该费用类型id");

        BeanUtils.copyProperties(costEntity,result);

        result.setUpdateUser(currentUserId);
        result.setUpdateTime(new Timestamp(new Date().getTime()));

        return result;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CostEntity getCost(CostEntity param) throws Exception{
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CostEntity> listCostEntity(Map<String, Object> paramMap) throws Exception{
        return this.costMapper.listCostEntity(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer listCostEntityCount(Map<String, Object> paramMap) throws Exception {
        return this.costMapper.listCostEntityCount(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public synchronized String checkCostExist(CostEntity costEntity) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        if(costEntity.getId() == null){

            CostEntity param = new CostEntity();

            param.setName(costEntity.getName());
            if( this.dbService.get(param) != null
                    ) stringBuilder.append("费用类型名已存在");

        }else{
            CostEntity param = new CostEntity();
            CostEntity result = null;

            param.setName(costEntity.getName());
            result = this.dbService.get(param);
            if( result != null && result.getId() != costEntity.getId() )
                stringBuilder.append("费用类型名已存在");

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
        this.costMapper.batchBan(paramMap);

    }
    
}
