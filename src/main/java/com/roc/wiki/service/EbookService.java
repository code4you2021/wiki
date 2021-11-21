package com.roc.wiki.service;

import com.roc.wiki.domain.Ebook;
import com.roc.wiki.domain.EbookExample;
import com.roc.wiki.mapper.EbookMapper;
import com.roc.wiki.req.EbookReq;
import com.roc.wiki.resp.EbookResp;
import com.roc.wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }

    public List<Ebook> listLike(String name) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + name + "%");
        return ebookMapper.selectByExample(ebookExample);
    }

    // 返回类。
    public List<EbookResp> listLikeResp(String name) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + name + "%");
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> list = CopyUtil.copyList(ebooks, EbookResp.class);
        return list;
    }
}
