package org.xxpay.mch.user.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchOperatorHandover;
import org.xxpay.mch.common.ctrl.BaseController;

/** 交班接口 **/
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/handover_history")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchHandoverHistoryController extends BaseController {

    private final static MyLog _log = MyLog.getLog(MchHandoverHistoryController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /** list */
    @RequestMapping(value = "/list")
    public XxPayResponse list() {

        //搜索条件
        MchOperatorHandover queryRecord = getObject(MchOperatorHandover.class);

        LambdaQueryWrapper<MchOperatorHandover> lambda = new QueryWrapper<MchOperatorHandover>().lambda();
        lambda.eq(MchOperatorHandover::getMchId, getUser().getBelongInfoId());
        if(getUser().getIsSuperAdmin() == MchConstant.PUB_NO){ //非管理员 仅查询自己的交接班记录
            lambda.eq(MchOperatorHandover::getUserId, getUser().getUserId());
        }

        if (queryRecord.getUserId() != null) {
            lambda.eq(MchOperatorHandover::getUserId, queryRecord.getUserId());
        }

        if (StringUtils.isNotBlank(queryRecord.getUserName())) {
            lambda.like(MchOperatorHandover::getUserName, queryRecord.getUserName());
        }

        if (queryRecord.getStoreId() != null) {
            lambda.eq(MchOperatorHandover::getStoreId, queryRecord.getStoreId());
        }

        if (queryRecord.getWorkStartTime() != null) {
            lambda.ge(MchOperatorHandover::getWorkEndTime, queryRecord.getWorkStartTime());
        }

        if (queryRecord.getWorkEndTime() != null) {
            lambda.le(MchOperatorHandover::getWorkEndTime, queryRecord.getWorkEndTime());
        }

        lambda.orderByDesc(MchOperatorHandover::getCreateTime);

        IPage<MchOperatorHandover> list = rpcCommonService.rpcMchOperatorHandoverService.page(getIPage(), lambda);
        return PageRes.buildSuccess(list);

    }

    /** get */
    @RequestMapping(value = "/get")
    public XxPayResponse get() {

        Long id = getValLongRequired("id");
        MchOperatorHandover record = rpcCommonService.rpcMchOperatorHandoverService.getById(id);

        if(record == null) throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        if(getUser().getIsSuperAdmin() == MchConstant.PUB_NO && !record.getUserId().equals(getUser().getUserId())){
            throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        }
        return XxPayResponse.buildSuccess(record);
    }


}