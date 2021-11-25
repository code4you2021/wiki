package com.roc.wiki.controller;

import com.roc.wiki.req.EbookReq;
import com.roc.wiki.resp.CommonResp;
import com.roc.wiki.resp.EbookResp;
import com.roc.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/ebook/list")
    public CommonResp ebookLikeListResp(EbookReq req) {
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> ebookList = ebookService.listLike(req.getName());
        resp.setContent(ebookList);
        return resp;
    }
}
