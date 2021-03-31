package com.aj.learning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ItemHtmlService {

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void asyncCreateHtml(Long spuId) {
        PrintWriter printWriter = null;
        try {
            //加载数据
            Map<String, Object> goodsMap = new HashMap<String, Object>();
            //获取上下文
            Context context = new Context();
            //数据存上下文
            context.setVariables(goodsMap);

            //输出流 生成静态页面 放到 Nginx html目录下面item文件夹中
            printWriter = new PrintWriter(new File("xxxx\\html\\item\\" + spuId + ".html"));
            //执行页静态化
            templateEngine.process("item",context,printWriter);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if(printWriter != null){
                printWriter.close();
            }
        }
    }
}
