package com.zsw.controllers;

import com.zsw.controller.BaseController;
import com.zsw.utils.DictionaryStaticURLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangshaowei on 2020/5/31.
 */
@RestController
@RequestMapping(DictionaryStaticURLUtil.goodsControler)
public class GoodsControler extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(GoodsControler.class);










    @Override
    public Logger getLOG(){
        return this.LOG;
    }

}
