package com.roc.wiki.controller;

import com.roc.wiki.req.EbookQueryReq;
import com.roc.wiki.req.EbookSaveReq;
import com.roc.wiki.resp.CommonResp;
import com.roc.wiki.resp.EbookQueryResp;
import com.roc.wiki.resp.PageResp;
import com.roc.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/ebook/list")
    public CommonResp ebookList(EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> ebookList = ebookService.listLike(req);
        resp.setContent(ebookList);
        return resp;
    }

    // PostMapping json方式的提交 使用  @RequestBody 注解才能接收到
    @PostMapping("/ebook/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @DeleteMapping("/ebook/delete/{id}")
    public CommonResp delete(@PathVariable("id") Long id) {
        CommonResp resp = new CommonResp<>();
        int delete = ebookService.delete(id);
        if (delete == 0) {
            resp.setSuccess(false);
        }
        return resp;
    }
}
