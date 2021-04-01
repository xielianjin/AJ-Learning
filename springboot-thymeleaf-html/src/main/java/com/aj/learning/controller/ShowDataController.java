package com.aj.learning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/item")
public class ShowDataController {

    @GetMapping("/{itemId}")
    public String showDate(@PathVariable("itemId") Long itemId){
        return "返回ID为" + itemId + "的数据";
    }

}
