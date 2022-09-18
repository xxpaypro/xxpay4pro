package org.xxpay.task.reconciliation.channel.jdpay;

import org.springframework.stereotype.Service;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.task.reconciliation.channel.BaseBill;

/**
 * @author: dingzhiwei
 * @date: 18/1/19
 * @description:
 */
@Service
public class JdpayBillService extends BaseBill {

    private static final MyLog _log = MyLog.getLog(JdpayBillService.class);

    @Override
    public String getChannelName() {
        return null;
    }
}
