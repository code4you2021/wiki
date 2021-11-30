package com.roc.wiki.controller;

import com.roc.wiki.req.CategoryQueryReq;
import com.roc.wiki.req.CategorySaveReq;
import com.roc.wiki.resp.CommonResp;
import com.roc.wiki.resp.CategoryQueryResp;
import com.roc.wiki.resp.PageResp;
import com.roc.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp categoryList(@Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> categoryList = categoryService.listLike(req);
        resp.setContent(categoryList);
        return resp;
    }

    // PostMapping json方式的提交 使用  @RequestBody 注解才能接收到
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {

        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable("id") Long id) {
        CommonResp resp = new CommonResp<>();
        int delete = categoryService.delete(id);
        if (delete == 0) {
            resp.setSuccess(false);
        }
        return resp;
    }

}
