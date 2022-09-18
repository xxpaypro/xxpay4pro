package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.IsvSettConfig;
import org.xxpay.core.entity.OrderProfitTask;

import java.util.Date;

/**
 * <p>
 * 服务商结算跑批任务表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-21
 */
public interface IOrderProfitTaskService extends IService<OrderProfitTask> {


    /**
     * @description:  计算服务商和代理商的分润情况
     * @Date 2019/9/23 20:53
     * @param isvSettConfig  服务商配置信息
     * @param nowDate0 当前时间： 00:00:00
     */
    void calIsvAndAgentsProfit(IsvSettConfig isvSettConfig, Date nowDate0);


    /**
     * 更新结算任务为[已结算]状态
     * @param taskId
     * @param settImgPath
     * @return
     */
    boolean updateSettFinish(Long taskId, String settImgPath);


}
