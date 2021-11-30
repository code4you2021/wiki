package com.roc.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.roc.wiki.domain.Category;
import com.roc.wiki.domain.CategoryExample;
import com.roc.wiki.mapper.CategoryMapper;
import com.roc.wiki.req.CategoryQueryReq;
import com.roc.wiki.req.CategorySaveReq;
import com.roc.wiki.resp.CategoryQueryResp;
import com.roc.wiki.resp.PageResp;
import com.roc.wiki.util.CopyUtil;
import com.roc.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private SnowFlake snowFlake;

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    public List<Category> list() {
        return categoryMapper.selectByExample(null);
    }

    // 查询Category

    public PageResp<CategoryQueryResp> listLike(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
//        if (!ObjectUtils.isEmpty(req.getName())) {
//            criteria.andNameLike("%" + req.getName() + "%");
//        }
        // pageNum 是从1开始 只对第一个遇到的sql起作用，比如只对下面第一个categoryMapper.selectByExample起作用
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categorys = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> info = new PageInfo<>(categorys);
        log.info("总行数：{}", info.getTotal());
        log.info("总页数：{}", info.getPages());
        PageResp<CategoryQueryResp> pageResp = new PageResp();
        List<CategoryQueryResp> list = CopyUtil.copyList(categorys, CategoryQueryResp.class);
        pageResp.setTotal(info.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    // 保存category
    // 用于新增和更新
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public int delete(Long id) {
        return categoryMapper.deleteByPrimaryKey(id);
    }

}
