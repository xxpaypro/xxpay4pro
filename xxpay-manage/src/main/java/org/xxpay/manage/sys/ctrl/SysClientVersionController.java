package org.xxpay.manage.sys.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.SysClientVersion;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/clientVersion")
public class SysClientVersionController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /** 查询单条记录 */
    @RequestMapping("/get")
    public XxPayResponse get() {
        SysClientVersion dbRecord = rpc.sysClientVersionService.getById(getValLongRequired( "vid"));
        return XxPayResponse.buildSuccess(dbRecord);
    }

    /** 列表查询 **/
    @RequestMapping("/list")
    public PageRes list() {

        //前台传入查询条件
        SysClientVersion record = getObject(SysClientVersion.class);

        LambdaQueryWrapper<SysClientVersion> condition = new LambdaQueryWrapper<>();
        condition.orderByDesc(SysClientVersion::getVid); // 按照vid降序查询

        //封装查询条件
        if(StringUtils.isNotEmpty(record.getClientType())) condition.eq(SysClientVersion::getClientType, record.getClientType());
        if(StringUtils.isNotEmpty(record.getVersionSN())) condition.eq(SysClientVersion::getVersionSN, record.getVersionSN());
        IPage<SysClientVersion> list = rpc.sysClientVersionService.page(getIPage(), condition);
        return PageRes.buildSuccess(list);
    }


    @RequestMapping("/add")
    @MethodLog( remark = "新增客户端版本" )
    public BizResponse add() {

        SysClientVersion sysClientVersion = getObject(SysClientVersion.class);

        //根据 客户端类型 & 版本序列号 查询是否存在
        int queryDbRow = rpc.sysClientVersionService.count(new QueryWrapper<SysClientVersion>().lambda()
                .eq(SysClientVersion::getClientType, sysClientVersion.getClientType())
                .eq(SysClientVersion::getVersionSN, sysClientVersion.getVersionSN())
        );
        if(queryDbRow > 0) throw ServiceException.build(RetEnum.RET_MCH_CUR_VERSION_EXISTS);

        boolean isTrue = rpc.sysClientVersionService.save(sysClientVersion);
        if(isTrue){
            return BizResponse.buildSuccess();
        }
        return BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
    }

    @RequestMapping("/delete")
    @MethodLog( remark = "删除客户端版本" )
    public BizResponse delete() {

        boolean isTrue = rpc.sysClientVersionService.removeById(getValLongRequired( "vid"));
        if(isTrue){
            return BizResponse.buildSuccess();
        }
        return BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
    }

}