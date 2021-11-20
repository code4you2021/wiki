package com.roc.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Value("${test.hello:Test}")
    private String hello;

    @GetMapping("demo")
    public String Demo() {
        return "demo" + hello;
    }
}
