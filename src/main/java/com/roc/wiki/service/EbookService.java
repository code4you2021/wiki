package com.roc.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.roc.wiki.domain.Ebook;
import com.roc.wiki.domain.EbookExample;
import com.roc.wiki.mapper.EbookMapper;
import com.roc.wiki.req.EbookQueryReq;
import com.roc.wiki.req.EbookSaveReq;
import com.roc.wiki.resp.EbookQueryResp;
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
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;
    @Resource
    private SnowFlake snowFlake;

    private static final Logger log = LoggerFactory.getLogger(EbookService.class);

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }

    // 查询Ebook
    public PageResp<EbookQueryResp> listLike(EbookQueryReq req) {

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // pageNum 是从1开始 只对第一个遇到的sql起作用，比如只对下面第一个ebookMapper.selectByExample起作用
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> info = new PageInfo<>(ebooks);
        log.info("总行数：{}", info.getTotal());
        log.info("总页数：{}", info.getPages());
        PageResp<EbookQueryResp> pageResp = new PageResp();
        List<EbookQueryResp> list = CopyUtil.copyList(ebooks, EbookQueryResp.class);
        pageResp.setTotal(info.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    // 保存ebook
    // 用于新增和更新
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        } else {
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    public int delete(Long id) {
        return ebookMapper.deleteByPrimaryKey(id);
    }
}
