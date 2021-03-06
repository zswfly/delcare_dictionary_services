package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.ContainerEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.services.IContainerService;
import com.zsw.utils.*;
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
@RequestMapping(DictionaryStaticURLUtil.containerController)
public class ContainerController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(ContainerController.class);

    @Autowired
    IContainerService containerService;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value= DictionaryStaticURLUtil.containerController_newContainer,
            method= RequestMethod.POST)
    //    @Permission(code = "dectionary.containerController.newContainer",name = "新增集装箱",description ="新增集装箱"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.containerController + DictionaryStaticURLUtil.containerController_newContainer)
    public String newContainer(ContainerEntity containerEntity, @RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();

            String check =this.containerService.checkContainerExist(containerEntity);
            if(StringUtils.isNotBlank(check) && StringUtils.isNotEmpty(check)){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
            }

            this.containerService.newContainer(containerEntity,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("新增成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }


    @RequestMapping(value=DictionaryStaticURLUtil.containerController_updateContainer,
            method= RequestMethod.PUT)
    //    @Permission(code = "dectionary.containerController.updateContainer",name = "更新集装箱",description ="更新集装箱"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.containerController + DictionaryStaticURLUtil.containerController_updateContainer)
    public String updateContainer(ContainerEntity containerEntity,@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();

            String check =this.containerService.checkContainerExist(containerEntity);
            if(StringUtils.isNotBlank(check) && StringUtils.isNotEmpty(check)){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
            }

            this.containerService.updateContainer(containerEntity,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }




    @RequestMapping(value=DictionaryStaticURLUtil.containerController_getContainer+"/{containerId}",
            method= RequestMethod.GET)
    //    @Permission(code = "dectionary.containerController.getContainer",name = "获取单个集装箱",description ="获取单个集装箱"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.containerController + DictionaryStaticURLUtil.containerController_getContainer)
    public String getContainer(@PathVariable Integer containerId) throws Exception {
        try {

            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();

            ContainerEntity containerEntity = new ContainerEntity();
            containerEntity.setId(containerId);
            containerEntity = this.containerService.getContainer(containerEntity);
            if(containerEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("该id没集装箱类型");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setData(containerEntity);
            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=DictionaryStaticURLUtil.containerController_containerPage,
            method= RequestMethod.GET)
    //    @Permission(code = "dectionary.containerController.containerPage",name = "搜索集装箱",description ="搜索集装箱"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.containerController + DictionaryStaticURLUtil.containerController_containerPage)
    public String containerPage(NativeWebRequest request) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();
            Map<String,Object> paramMap = new HashMap<String, Object>();

            String status = request.getParameter("status");
            if(status !=null && StringUtils.isNotEmpty(status)) {
                paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
            }
            String containerName = request.getParameter("name");
            if(containerName !=null && StringUtils.isNotEmpty(containerName)) {
                paramMap.put("containerName", containerName);
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
            List<ContainerEntity> items = this.containerService.listContainerEntity(paramMap);
            Integer total = this.containerService.listContainerEntityCount(paramMap);
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

    @RequestMapping(value=DictionaryStaticURLUtil.containerController_batchBan,
            method= RequestMethod.PUT)
    //@Permission(code = "dectionary.containerController.batchBan",name = "批量禁用/恢复集装箱",description ="批量禁用/恢复集装箱"
    //    ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.containerController + UserStaticURLUtil.containerController_batchBan)
    public String batchBan( @RequestParam Map<String, String> params , @RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();
            String ids = params.get("ids");
            String type = params.get("type");
            if(ids == null || type == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("参数不全");
                return gson.toJson(responseJson);
            }else{
                List<Integer> list = Arrays.asList(gson.fromJson(ids, Integer[].class));
                this.containerService.batchBan(list,type,currentUserId);
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
