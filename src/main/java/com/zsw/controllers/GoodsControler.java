package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.GoodsEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.services.IGoodsService;
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
@RequestMapping(DictionaryStaticURLUtil.goodsControler)
public class GoodsControler extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(GoodsControler.class);

    @Autowired
    IGoodsService goodsService;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value= DictionaryStaticURLUtil.goodsControler_newGoods,
            method= RequestMethod.POST)
    //    @Permission(code = "dectionary.goodsControler.newGoods",name = "新增货物",description ="新增货物"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.goodsControler + DictionaryStaticURLUtil.goodsControler_newGoods)
    public String newGoods(GoodsEntity goodsEntity, @RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            String check =this.goodsService.checkGoodsExist(goodsEntity);
            if(StringUtils.isNotBlank(check) && StringUtils.isNotEmpty(check)){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
            }


            this.goodsService.newGoods(goodsEntity,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("新增成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }


    @RequestMapping(value=DictionaryStaticURLUtil.goodsControler_updateGoods,
            method= RequestMethod.PUT)
    //    @Permission(code = "dectionary.goodsControler.updateGoods",name = "更新货物",description ="更新货物"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.goodsControler + DictionaryStaticURLUtil.goodsControler_updateGoods)
    public String updateGoods(GoodsEntity goodsEntity,@RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            String check =this.goodsService.checkGoodsExist(goodsEntity);
            if(StringUtils.isNotBlank(check) && StringUtils.isNotEmpty(check)){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage(check);
            }

            this.goodsService.updateGoods(goodsEntity,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("更新成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }




    @RequestMapping(value=DictionaryStaticURLUtil.goodsControler_getGoods+"/{goodsId}",
            method= RequestMethod.GET)
    //    @Permission(code = "dectionary.goodsControler.getGoods",name = "获取单个货物",description ="获取单个货物"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.goodsControler + DictionaryStaticURLUtil.goodsControler_getGoods)
    public String getGoods(@PathVariable Integer goodsId) throws Exception {
        try {

            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            GoodsEntity goodsEntity = new GoodsEntity();
            goodsEntity.setId(goodsId);
            goodsEntity = this.goodsService.getGoods(goodsEntity);
            if(goodsEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("该id没货物类型");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setData(goodsEntity);
            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=DictionaryStaticURLUtil.goodsControler_goodsPage,
            method= RequestMethod.GET)
    //    @Permission(code = "dectionary.goodsControler.goodsPage",name = "搜索货物",description ="搜索货物"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.goodsControler + DictionaryStaticURLUtil.goodsControler_goodsPage)
    public String goodsPage(NativeWebRequest request) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();
            Map<String,Object> paramMap = new HashMap<String, Object>();

            String status = request.getParameter("status");
            if(status !=null && StringUtils.isNotEmpty(status)) {
                paramMap.put("status", Integer.valueOf(NumberUtils.toInt(status, CommonStaticWord.Normal_Status_0)));
            }
            String goodsName = request.getParameter("goodsName");
            if(goodsName !=null && StringUtils.isNotEmpty(goodsName)) {
                paramMap.put("goodsName", goodsName);
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
            List<GoodsEntity> items = this.goodsService.listGoodsEntity(paramMap);
            Integer total = this.goodsService.listGoodsEntityCount(paramMap);
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

    @RequestMapping(value=DictionaryStaticURLUtil.goodsControler_batchBan,
            method= RequestMethod.PUT)
    //@Permission(code = "dectionary.goodsControler.batchBan",name = "批量禁用/恢复货物",description ="批量禁用/恢复货物"
    //    ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.goodsControler + UserStaticURLUtil.goodsControler_batchBan)
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
                this.goodsService.batchBan(list,type,currentUserId);
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
