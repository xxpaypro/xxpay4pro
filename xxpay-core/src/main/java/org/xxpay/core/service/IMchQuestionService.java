package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchQuestion;

/**
 * <p>
 * 商户常见问题表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
public interface IMchQuestionService extends IService<MchQuestion> {

    /**
     * 常见问题列表
     * @param mchQuestion
     * @param page
     * @return
     */
    IPage<MchQuestion> list(MchQuestion mchQuestion, Page page);

}
