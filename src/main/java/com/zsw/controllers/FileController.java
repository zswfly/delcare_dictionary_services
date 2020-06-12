package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.FileEntity;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.services.IFileService;
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
@RequestMapping(DictionaryStaticURLUtil.fileController)
public class FileController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

    @Autowired
    IFileService fileService;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value= DictionaryStaticURLUtil.fileController_newFile,
            method= RequestMethod.POST)
    //    @Permission(code = "dectionary.fileController.newFile",name = "新增文件",description ="新增文件"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.fileController + DictionaryStaticURLUtil.fileController_newFile)
    public String newFile(FileEntity fileEntity, @RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();


            this.fileService.newFile(fileEntity,currentUserId);

            responseJson.setCode(ResponseCode.Code_200);
            responseJson.setMessage("新增成功");

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }






    @RequestMapping(value=DictionaryStaticURLUtil.fileController_getFile+"/{fileId}",
            method= RequestMethod.GET)
    //    @Permission(code = "dectionary.fileController.getFile",name = "获取单个文件",description ="获取单个文件"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.fileController + DictionaryStaticURLUtil.fileController_getFile)
    public String getFile(@PathVariable Integer fileId) throws Exception {
        try {

            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();

            FileEntity fileEntity = new FileEntity();
            fileEntity.setId(fileId);
            fileEntity = this.fileService.getFile(fileEntity);
            if(fileEntity == null){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("该id没文件记录");
            }else{
                responseJson.setCode(ResponseCode.Code_200);
                responseJson.setData(fileEntity);
            }

            return gson.toJson(responseJson);
        }catch (Exception e){
            CommonUtils.ErrorAction(LOG,e);
            return CommonUtils.ErrorResposeJson(null);
        }
    }

    @RequestMapping(value=DictionaryStaticURLUtil.fileController_filePage,
            method= RequestMethod.GET)
    //    @Permission(code = "dectionary.fileController.filePage",name = "搜索文件",description ="搜索文件"
//            ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.fileController + DictionaryStaticURLUtil.fileController_filePage)
    public String filePage(NativeWebRequest request) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();
            Map<String,Object> paramMap = new HashMap<String, Object>();

            String fileName = request.getParameter("name");
            if(fileName !=null && StringUtils.isNotEmpty(fileName)) {
                paramMap.put("fileName", fileName);
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
            List<FileEntity> items = this.fileService.listFileEntity(paramMap);
            Integer total = this.fileService.listFileEntityCount(paramMap);
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

    @RequestMapping(value=DictionaryStaticURLUtil.fileController_deleteFile,
            method= RequestMethod.PUT)
    //@Permission(code = "dectionary.fileController.deleteFile",name = "批量禁用/恢复文件",description ="批量禁用/恢复文件"
    //    ,url=CommonStaticWord.dictionaryServices + DictionaryStaticURLUtil.fileController + UserStaticURLUtil.fileController_deleteFile)
    public String deleteFile( @RequestParam Map<String, String> params , @RequestHeader("userId") Integer currentUserId) throws Exception {
        try {
            ResponseJson responseJson = new ResponseJson();
            Gson gson = CommonUtils.getGson();
            String ids = params.get("ids");
            if(ids == null ){
                responseJson.setCode(ResponseCode.Code_Bussiness_Error);
                responseJson.setMessage("参数不全");
                return gson.toJson(responseJson);
            }else{
                List<Integer> list = Arrays.asList(gson.fromJson(ids, Integer[].class));
                this.fileService.deleteFileByIds(list);
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
