package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.SysArticle;

import java.util.List;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-05-15
 */
public interface ISysArticleService extends IService<SysArticle> {

    IPage<SysArticle> list(SysArticle sysArticle, IPage iPage);

    Integer batchDelete(List<Long> ids);

    int delete(Long id);
}
