package com.agile.train.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mengting Lu
 * @date 2022/1/23 9:58
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello world!";
    }

    @GetMapping("/h")
    public String h(@RequestParam String inp){
        return "hello "+inp+"!";
    }
}
