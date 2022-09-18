package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.SysArticle;
import org.xxpay.core.service.ISysArticleService;
import org.xxpay.service.dao.mapper.SysArticleMapper;

import java.util.List;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-05-15
 */
@Service
public class SysArticleServiceImpl extends ServiceImpl<SysArticleMapper, SysArticle> implements ISysArticleService {
    @Autowired
    private SysArticleMapper sysArticleMapper;

    @Override
    public int delete(Long id) {
        return sysArticleMapper.deleteById(id);
    }

    @Override
    public Integer batchDelete(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)) return 0;
        int count = 0;
        for(Long id : ids) {
            count += delete(id);
        }
        return count;
    }
    @Override
    public IPage<SysArticle> list(SysArticle sysArticle, IPage iPage) {
        LambdaQueryWrapper<SysArticle> lambda = new QueryWrapper<SysArticle>().lambda();
        if (sysArticle.getMchId() != null) lambda.eq(SysArticle::getMchId, sysArticle.getMchId());
        if (StringUtils.isNotBlank(sysArticle.getTitle())) lambda.eq(SysArticle::getTitle, sysArticle.getTitle());
        if (sysArticle.getArticleType() != null && sysArticle.getArticleType() != -99) lambda.eq(SysArticle::getArticleType, sysArticle.getArticleType());
        lambda.orderByDesc(SysArticle::getCreateTime);
        return page(iPage, lambda);
    }
}
