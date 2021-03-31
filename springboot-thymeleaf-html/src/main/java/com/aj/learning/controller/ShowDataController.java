package com.aj.learning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/item")
public class ShowDataController {

    @GetMapping("/{id}")
    public String showDate(@PathVariable("id") Long id){
        return "返回ID为" + id + "的数据";
    }

}
