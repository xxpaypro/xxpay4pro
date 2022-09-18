package org.xxpay.task.reconciliation.scheduled;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.CheckBatch;
import org.xxpay.task.common.service.RpcCommonService;
import org.xxpay.task.reconciliation.channel.BillInterface;
import org.xxpay.task.reconciliation.entity.ReconciliationEntity;
import org.xxpay.task.reconciliation.service.ReconciliationService;
import org.xxpay.task.reconciliation.util.SpringUtil;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/3
 * @description:
 */
@Component
public class ReconciliationScheduled {

    private static final MyLog _log = MyLog.getLog(ReconciliationScheduled.class);

    @Autowired
    private ReconciliationService reconciliationService;

    //@Scheduled(cron="0 0/1 * * * ?") // 每分钟执行一次
    @Scheduled(cron="0 15 10 ? * *")   // 每日10:15执行
    public void settDailyCollectTask() {
        _log.info("执行对账,开始...");

        Map<String, Channel> handleMap = new HashedMap();
        List<Channel> channelList = new LinkedList<>();

        // 1.得到所有需要对账的三方支付渠道账号
        // TODO 以后版本会增加三方对账功能

        for(Channel channel : channelList) {
            String channelMchId = channel.getChannelMchId();
            // 判断是否执行过对账
            if(handleMap.get(channelMchId) != null) continue;
            handleMap.put(channelMchId, channel);

            if(StringUtils.isBlank(channelMchId)
                    || StringUtils.isBlank(channel.getParam())) continue;

            Date billDate = DateUtil.addDay(new Date(), -1);
            // 检测是否对过账
            boolean checked = reconciliationService.isChecked(channelMchId, billDate);
            if (checked) {
                _log.info("账单日[" + DateUtil.date2Str(billDate) + "],渠道商户ID[" + channelMchId + "],已经对过账，不能再次发起自动对账。");
                continue;
            }
            // 创建对账批次对象
            CheckBatch batch = new CheckBatch();
            batch.setBatchNo(MySeq.getCheck());
            batch.setBillDate(billDate);
            batch.setChannelMchId(channelMchId);
            batch.setBillType((byte) 1);
            batch.setBankType(channel.getChannelType());

            // 2.下载对账文件
            String channelType = channel.getChannelType();
            BillInterface billInterface = null;
            try {
                billInterface = (BillInterface) SpringUtil.getBean(channelType.toLowerCase() +  "BillService");
            }catch (Exception e) {
                //_log.error(e, "");
            }
            if(billInterface == null) {
                _log.info(channelType.toLowerCase() +  "BillService" + "没有对应的实现");
                continue;
            }
            JSONObject param = new JSONObject();
            param.put("payParam", channel.getParam());
            JSONObject retObj = billInterface.downloadBill(param, batch);
            List<ReconciliationEntity> bankList = null;
            if(XXPayUtil.isSuccess(retObj)) {
                bankList = retObj.getObject("bill", List.class);
            }
            System.out.println(retObj);

            if(CollectionUtils.isEmpty(bankList)) {
                _log.info("channelMchId={},银行侧对账数据为空.不用对账", channelMchId);
                //continue;
            }

            // 对账
            reconciliationService.check(bankList, channelMchId, batch);

        }

        // 删除三天前的缓冲数据
        reconciliationService.validateScratchPool();

        _log.info("执行对账,结束。");
    }

    class Channel {

        private String channelMchId;
        private String channelType;
        private String param;
        public Channel(){}

        public Channel(String channelMchId, String channelType, String param) {
            this.channelMchId = channelMchId;
            this.channelType = channelType;
            this.param = param;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getChannelType() {
            return channelType;
        }

        public void setChannelType(String channelType) {
            this.channelType = channelType;
        }

        public String getChannelMchId() {
            return channelMchId;
        }

        public void setChannelMchId(String channelMchId) {
            this.channelMchId = channelMchId;
        }
    }

}
