package com.aj.learning.controller;

import com.aj.learning.service.ItemHtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("item")
public class ThymeleafController {

    @Autowired
    private ItemHtmlService itemHtmlService;

    @GetMapping("{id}.html")
    public String itemHtml(Model model, @PathVariable("id") Long id){
        //假数据
        Map<String,Object> loadData = new HashMap<String,Object>();
        loadData.put("test1","测试1");
        loadData.put("test2","测试2");
        model.addAllAttributes(loadData);

        //生成商品html页，并上传到静态页面服务器,此处可以加一个分布式锁，防止多次生成，浪费资源
        this.itemHtmlService.asyncCreateHtml(id);
        return "items";
    }


}
