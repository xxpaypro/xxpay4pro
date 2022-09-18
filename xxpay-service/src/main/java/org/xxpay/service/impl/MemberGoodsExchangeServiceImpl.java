package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.util.MemberSeq;
import org.xxpay.core.entity.MchPointsGoods;
import org.xxpay.core.entity.MemberGoodsExchange;
import org.xxpay.core.entity.MemberPoints;
import org.xxpay.core.entity.MemberPointsHistory;
import org.xxpay.core.service.IMemberGoodsExchangeService;
import org.xxpay.core.service.IMemberPointsHistoryService;
import org.xxpay.core.service.IMemberPointsService;
import org.xxpay.service.dao.mapper.MchPointsGoodsMapper;
import org.xxpay.service.dao.mapper.MemberGoodsExchangeMapper;
import org.xxpay.service.dao.mapper.MemberPointsHistoryMapper;

import javax.xml.ws.Action;
import java.util.Date;

/**
 * <p>
 * 积分商品会员兑换表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MemberGoodsExchangeServiceImpl extends ServiceImpl<MemberGoodsExchangeMapper, MemberGoodsExchange> implements IMemberGoodsExchangeService {

    @Autowired
    private MemberGoodsExchangeMapper memberGoodsExchangeMapper;

    @Autowired
    private MchPointsGoodsMapper mchPointsGoodsMapper;

    @Autowired
    private IMemberPointsService memberPointsService;

    @Autowired
    private MemberPointsHistoryMapper memberPointsHistoryMapper;

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int addMbrGoods(MchPointsGoods mchPointsGoods, Long memberId, String memberNo, Long mchId) {

        int count = 0;
        //插入积分商品会员兑换记录
        String exchangeNo = MemberSeq.getExchangeNo();
        MemberGoodsExchange goods = new MemberGoodsExchange();
        goods.setExchangeNo(exchangeNo);
        goods.setGoodsId(mchPointsGoods.getGoodsId());
        goods.setMemberId(memberId);
        goods.setMemberNo(memberNo);
        goods.setMchId(mchId);
        goods.setExchangeTime(new Date());
        goods.setStatus(MchConstant.INTEGRAL_COMMODITY_NOT_EXCHANGE);
        goods.setGoodsName(mchPointsGoods.getGoodsName());
        goods.setPoints(mchPointsGoods.getPoints());
        count = memberGoodsExchangeMapper.insert(goods);
        if (count !=1) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

        //扣除对应积分
        MemberPoints updatePoints = new MemberPoints();
        updatePoints.setMemberId(memberId);//会员ID
        updatePoints.setTotalConsumePoints(mchPointsGoods.getPoints());//消费积分累加
        updatePoints.setPoints(0 - mchPointsGoods.getPoints());//可用积分
        count = memberPointsService.updatePointByMemberId(updatePoints);
        if (count !=1) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

        //查询最新会员积分
        MemberPoints dbMemberPoints = memberPointsService.getById(memberId);
        if (dbMemberPoints.getPoints() < 0) {
            throw new ServiceException(RetEnum.RET_MBR_POINTS_NOT_ENOUGH);
        }

        //插入积分流水
        MemberPointsHistory history = new MemberPointsHistory();
        history.setMemberId(memberId);  //会员ID
        history.setMemberNo(memberNo);  //会员No
        history.setChangePoints(0 - mchPointsGoods.getPoints());  //变动积分
        history.setPoints(dbMemberPoints.getPoints() + mchPointsGoods.getPoints());  //变更前积分
        history.setAfterPoints(dbMemberPoints.getPoints());  //变更后积分
        history.setMchId(mchId);  //mchId
        history.setBizType(MchConstant.MCH_VIP_POINTS_HISTORY_BIZ_TYPE_EXCHANGE);  //业务类型1:积分商品兑换
        count = memberPointsHistoryMapper.insert(history);
        if (count !=1) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

        // 限制库存：兑换记录表新增一条记录，积分商品表库存减一，若剩余数量为0，修改状态为下架
        if (mchPointsGoods.getStockLimitType() == MchConstant.MCH_POINTS_GOODS_STOCK_LIMIT_TYPE_YES) {
            //商户积分商品数量减1，行锁
            MchPointsGoods updateRecord = new MchPointsGoods();
            updateRecord.setGoodsId(mchPointsGoods.getGoodsId());//积分商品ID
            updateRecord.setStockNum(-1L);//领取一张
            if(mchPointsGoodsMapper.updateStockNumByGoodsId(updateRecord) <= 0){
                throw new ServiceException(RetEnum.RET_MBR_POINTS_GOODS_GET_OVER);
            }
            MchPointsGoods dbMchGoods = mchPointsGoodsMapper.selectById(mchPointsGoods.getGoodsId());
            if (dbMchGoods.getStockNum() < 0) {
                throw new ServiceException(RetEnum.RET_MBR_POINTS_GOODS_GET_OVER);
            }

            //如果库存数量为0，更新状态为下架
            if (dbMchGoods.getStockNum() == 0) {
                MchPointsGoods updateRecord2 = new MchPointsGoods();
                updateRecord2.setGoodsId(mchPointsGoods.getGoodsId());
                updateRecord2.setStatus(MchConstant.MCH_POINTS_GOODS_STATE_DOWN);
                count = mchPointsGoodsMapper.updateById(updateRecord2);
                if (count != 1) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
            }
        }

        return 1;
    }

}
