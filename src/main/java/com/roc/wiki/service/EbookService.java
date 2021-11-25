package com.roc.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.roc.wiki.domain.Ebook;
import com.roc.wiki.domain.EbookExample;
import com.roc.wiki.mapper.EbookMapper;
import com.roc.wiki.req.EbookReq;
import com.roc.wiki.resp.EbookResp;
import com.roc.wiki.resp.PageResp;
import com.roc.wiki.util.CopyUtil;
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

    private static final Logger log = LoggerFactory.getLogger(EbookService.class);

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }

    // 返回类。
    public PageResp<EbookResp> listLike(EbookReq req) {

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
        PageResp<EbookResp> pageResp = new PageResp();
        List<EbookResp> list = CopyUtil.copyList(ebooks, EbookResp.class);
        pageResp.setTotal(info.getTotal());
        pageResp.setList(list);
        return pageResp;
    }
}
