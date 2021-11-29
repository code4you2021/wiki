package com.roc.wiki.controller;

import com.roc.wiki.req.EbookQueryReq;
import com.roc.wiki.req.EbookSaveReq;
import com.roc.wiki.resp.CommonResp;
import com.roc.wiki.resp.EbookResp;
import com.roc.wiki.resp.PageResp;
import com.roc.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/ebook/list")
    public CommonResp ebookList(EbookQueryReq req) {
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> ebookList = ebookService.listLike(req);
        resp.setContent(ebookList);
        return resp;
    }

    // json方式的提交 使用  @RequestBody 注解才能接收到
    @PostMapping("/ebook/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
}
