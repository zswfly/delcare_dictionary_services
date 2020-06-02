package com.zsw.services;

import com.zsw.daos.CrmObjectMapper;
import com.zsw.entitys.CrmObjectEntity;
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
public class CrmObjectServiceImpl implements ICrmObjectService,Serializable {

    private static final long serialVersionUID = -6878900781466827010L;

    @Autowired
    private IDBService dbService;

    @Resource
    private CrmObjectMapper crmObjectMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void newCrmObject(CrmObjectEntity crmObjectEntity, Integer currentUserId)throws Exception {
        crmObjectEntity.setId(null);
        crmObjectEntity.setMnemonicCode(PinyinUtils.getFirstSpell(crmObjectEntity.getName()));
        crmObjectEntity.setStatus(CommonStaticWord.Normal_Status_0);
        crmObjectEntity.setCreateUser(currentUserId);
        crmObjectEntity.setCreateTime(new Timestamp(new Date().getTime()));
        crmObjectEntity.setUpdateUser(currentUserId);
        crmObjectEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        this.dbService.save(crmObjectEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public CrmObjectEntity updateCrmObject(CrmObjectEntity crmObjectEntity, Integer currentUserId) throws Exception{
        if(crmObjectEntity == null
                || crmObjectEntity.getId() == null
                ){
            throw new Exception("参数错误");
        }

        CrmObjectEntity result = this.dbService.get(CrmObjectEntity.class,crmObjectEntity.getId());

        if(result == null) throw new Exception("没有该crm对象id");

        BeanUtils.copyProperties(crmObjectEntity,result);

        result.setUpdateUser(currentUserId);
        result.setUpdateTime(new Timestamp(new Date().getTime()));

        return result;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CrmObjectEntity getCrmObject(CrmObjectEntity param) throws Exception{
        return this.dbService.get(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CrmObjectEntity> listCrmObjectEntity(Map<String, Object> paramMap) throws Exception{
        return this.crmObjectMapper.listCrmObjectEntity(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer listCrmObjectEntityCount(Map<String, Object> paramMap) throws Exception {
        return this.crmObjectMapper.listCrmObjectEntityCount(paramMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public synchronized String checkCrmObjectExist(CrmObjectEntity crmObjectEntity) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        if(crmObjectEntity.getId() == null){

            CrmObjectEntity param = new CrmObjectEntity();

            param.setName(crmObjectEntity.getName());
            if( this.dbService.get(param) != null
                    ) stringBuilder.append("crm对象名已存在");

        }else{
            CrmObjectEntity param = new CrmObjectEntity();
            CrmObjectEntity result = null;

            param.setName(crmObjectEntity.getName());
            result = this.dbService.get(param);
            if( result != null && result.getId() != crmObjectEntity.getId() )
                stringBuilder.append("crm对象名已存在");

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
        this.crmObjectMapper.batchBan(paramMap);

    }
}
