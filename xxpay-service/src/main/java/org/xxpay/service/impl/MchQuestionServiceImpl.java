package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.MchQuestion;
import org.xxpay.core.service.IMchQuestionService;
import org.xxpay.service.dao.mapper.MchQuestionMapper;

/**
 * <p>
 * 商户常见问题表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@Service
public class MchQuestionServiceImpl extends ServiceImpl<MchQuestionMapper, MchQuestion> implements IMchQuestionService {

    @Override
    public IPage<MchQuestion> list(MchQuestion mchQuestion, Page page) {
        LambdaQueryWrapper<MchQuestion> lambda = new QueryWrapper<MchQuestion>().lambda();
        lambda.orderByDesc(MchQuestion::getCreateTime);
        lambda.eq(MchQuestion::getMchId, mchQuestion.getMchId());
        lambda.eq(MchQuestion::getStatus, MchConstant.PUB_YES);
        return page(page, lambda);
    }

}
