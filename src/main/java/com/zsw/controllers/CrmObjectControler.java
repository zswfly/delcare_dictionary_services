package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.CrmObjectEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.services.ICrmObjectService;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.CommonUtils;
import com.zsw.utils.DictionaryStaticURLUtil;
import com.zsw.utils.ResponseCode;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/5/31.
 */
@RestController
@RequestMapping(DictionaryStaticURLUtil.crmObjectControler)
public class CrmObjectControler extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(CrmObjectControler.class);

    @Autowired
    ICrmObjectService crmObjectService;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value= DictionaryStaticURLUtil.crmObjectControler_newCrmObject,
            method= RequestMethod.POST)
    //    @Permission(code = "dectionary.crmObjectControler.newCompany",name = "新增公司",description ="新增公司"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.crmObjectControler + DictionaryStaticURLUtil.crmObjectControler_newCrmObject)
    public String newCrmObject(CrmObjectEntity crmObjectEntity, @RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            String check =this.crmObjectService.checkCrmObjectExist(crmObjectEntity);
            if(StringUtils.isNotBlank(check) && StringUtils.isNotEmpty(check)){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
            }


            this.crmObjectService.newCrmObject(crmObjectEntity,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("新增成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }


    @RequestMapping(value=DictionaryStaticURLUtil.crmObjectControler_updateCrmObject,
            method= RequestMethod.PUT)
    //    @Permission(code = "dectionary.crmObjectControler.newCompany",name = "新增公司",description ="新增公司"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.crmObjectControler + DictionaryStaticURLUtil.crmObjectControler_newCrmObject)
    public String updateCrmObject(CrmObjectEntity crmObjectEntity,@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            String check =this.crmObjectService.checkCrmObjectExist(crmObjectEntity);
            if(StringUtils.isNotBlank(check) && StringUtils.isNotEmpty(check)){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
            }


            this.crmObjectService.updateCrmObject(crmObjectEntity,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }




    @RequestMapping(value=DictionaryStaticURLUtil.crmObjectControler_getCrmObject+"/{crmObjectId}",
            method= RequestMethod.GET)
    public String getCrmObject(@PathVariable Integer crmObjectId) throws Exception {
        try {

            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            CrmObjectEntity crmObjectEntity = new CrmObjectEntity();
            crmObjectEntity.setId(crmObjectId);
            crmObjectEntity = this.crmObjectService.getCrmObject(crmObjectEntity);
            if(crmObjectEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("该id没公司");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setData(crmObjectEntity);
            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=DictionaryStaticURLUtil.crmObjectControler_crmObjectPage,
            method= RequestMethod.GET)
    //    @Permission(code = "dectionary.crmObjectControler.newCompany",name = "新增公司",description ="新增公司"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.crmObjectControler + DictionaryStaticURLUtil.crmObjectControler_newCrmObject)
    public String crmObjectPage(NativeWebRequest request) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            Map<String,Object> paramMap = new HashMap<String, Object>();

            String status = request.getParameter("status");
            if(status !=null && StringUtils.isNotEmpty(status)) {
                paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
            }
            String crmObjectName = request.getParameter("crmObjectName");
            if(crmObjectName !=null && StringUtils.isNotEmpty(crmObjectName)) {
                paramMap.put("crmObjectName", crmObjectName);
            }

            String mnemonicCode = request.getParameter("mnemonicCode");
            if(mnemonicCode !=null && StringUtils.isNotEmpty(mnemonicCode)) {
                paramMap.put("mnemonicCode", mnemonicCode);
            }

            String beginCreateTime = request.getParameter("beginCreateTime");
            if(beginCreateTime !=null && StringUtils.isNotEmpty(beginCreateTime)) {
                paramMap.put("beginCreateTime", beginCreateTime);
            }
            String endCreateTime = request.getParameter("endCreateTime");
            if(endCreateTime !=null && StringUtils.isNotEmpty(endCreateTime)) {
                paramMap.put("endCreateTime", endCreateTime);
            }


            Integer currentPage = Integer.valueOf(NumberUtils.toInt(request.getParameter("currentPage"), 1));
            Integer pageSize = Integer.valueOf(NumberUtils.toInt(request.getParameter("pageSize"), 10));

            paramMap.put("start", (currentPage-1)*pageSize);
            paramMap.put("pageSize", pageSize);

            Map<String,Object> data = new HashMap<>();
            List<CrmObjectEntity> items = this.crmObjectService.listCrmObjectEntity(paramMap);
            Integer total = this.crmObjectService.listCrmObjectEntityCount(paramMap);
            data.put("items",items);
            data.put("total",total==null?0:total);
            responseJson.setData(data);
            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("搜索成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=DictionaryStaticURLUtil.crmObjectControler_batchBan,
            method= RequestMethod.PUT)
    //@Permission(code = "user.userController.batchBan",name = "批量禁用/恢复",description ="批量禁用/恢复用户"
    //    ,url=CommonStaticWord.userServices + UserStaticURLUtil.userController + UserStaticURLUtil.userController_batchBan)
    public String batchBan( @RequestParam Map<String, String> params , @RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            String ids = params.get("ids");
            String type = params.get("type");
            if(ids == null || type == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("参数不全");
                return gson.toJson(responseJson);
            }else{
                List<Integer> list = Arrays.asList(gson.fromJson(ids, Integer[].class));
                this.crmObjectService.batchBan(list,type,currentUserId);
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setMessage("更新成功");
                return gson.toJson(responseJson);
            }

        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }









    @Override
    public Logger getLOG(){
        return this.LOG;
    }

}
