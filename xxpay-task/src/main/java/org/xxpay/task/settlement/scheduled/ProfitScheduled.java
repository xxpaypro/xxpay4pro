package org.xxpay.task.settlement.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.IsvSettConfig;
import org.xxpay.task.common.service.RpcCommonService;

import java.util.Date;
import java.util.List;

/** 处理订单分润 - 定时任务 **/
@Component
public class ProfitScheduled {

    @Autowired
    private RpcCommonService rpc;
    

    private static final MyLog logger = MyLog.getLog(ProfitScheduled.class);


//    @Scheduled(cron="0 0/1 * * * ?")   //每分钟执行一次
    @Scheduled(cron="0 0/10 * * * ?")   //每10分钟执行一次  TODO 上线需更改为正常时间， 测试环境10分钟跑批/次
//    @Scheduled(cron="0 0 3 * * ?")   //每天的 03:00 启动定时任务
    public void allAgentSettDailyCollectTask() {
    	
        logger.info("执行分润计划,开始...");
        Long beginTime = System.currentTimeMillis();

        //查询所有今天 执行分润计划的服务商
        List<IsvSettConfig> settIsvSettList = rpc.rpcIsvSettConfigService.list(new LambdaQueryWrapper<IsvSettConfig>().le(IsvSettConfig::getNextSettDate, new Date()));

        logger.info("共{}条服务商记录", settIsvSettList.size());

        //当前时间 0点
        Date nowDate0 = DateUtil.str2date(DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYY_MM_DD) + " 00:00:00" );

        //遍历所有待分润的服务商集合
        for(IsvSettConfig isvSettConfig : settIsvSettList){

            try{

                rpc.rpcOrderProfitTaskService.calIsvAndAgentsProfit(isvSettConfig, nowDate0);


            }catch (Exception e){
                logger.error("执行服务商分润计划失败， 继续执行下一个服务商！ 当前isvId={}", e, isvSettConfig.getIsvId());
                continue;
            }
        }
        logger.info("执行分润计划,结束， 耗时：{}ms。", System.currentTimeMillis() - beginTime);
    }
}
