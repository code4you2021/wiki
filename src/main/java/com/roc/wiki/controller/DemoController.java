package com.roc.wiki.controller;

import com.roc.wiki.domain.Ebook;
import com.roc.wiki.resp.CommonResp;
import com.roc.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @Value("${test.hello:Test}")
    private String hello;

    @Autowired
    private EbookService ebookService;

    @GetMapping("demo")
    public String Demo() {
        return "demo" + hello;
    }

    @GetMapping("/ebook")
    public CommonResp ebookList() {
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        List<Ebook> ebookList = ebookService.list();
        resp.setContent(ebookList);
        return resp;
    }



}
