package org.xxpay.pay.task;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.util.IPUtility;
import org.xxpay.core.common.util.MyLog;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 2018/10/3
 * @description:
 */
@Component
public class ReissuceBase {

    private static final MyLog _log = MyLog.getLog(ReissuceBase.class);

    @Value("${task.reissue.trans.switch}")
    public String reissueTransSwitch;           // 转账补单任务开关

    @Value("${task.reissue.trans.ip}")
    public String reissueTransIP;               // 转账补单任务IP

    @Value("${task.reissue.agentpay.switch}")
    public String reissueAgentpayTaskSwitch;	// 代付补单任务开关

    @Value("${task.reissue.agentpay.ip}")
    public String reissueAgentpayTaskIp;		// 代付补单任务IP

    @Value("${task.reissue.pay.switch}")
    public String reissuePayTaskSwitch;	// 支付补单任务开关

    @Value("${task.reissue.pay.ip}")
    public String reissuePayTaskIp;		// 支付补单任务IP

    /**
     * 判断是否执行补单任务
     * @return
     */
    boolean isExcuteReissueTask(String reissueTaskSwitch, String reissueTaskIp) {
        if(!"true".equalsIgnoreCase(reissueTaskSwitch)) {
            return false;
        }
        try {
            List<String> ips = IPUtility.getIpAddrs();
            if(CollectionUtils.isEmpty(ips)) return false;
            if(ips.contains(reissueTaskIp)) {
                return true;
            }
            _log.info("reissueTaskIp={},localIps={}", reissueTaskIp, ips);
        } catch (Exception e) {
            _log.error(e, "获取本地IP异常");
            e.printStackTrace();
        }
        return false;
    }
}
