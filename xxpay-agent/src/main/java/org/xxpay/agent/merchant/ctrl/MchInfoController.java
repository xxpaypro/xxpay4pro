package org.xxpay.agent.merchant.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.SettBankAccount;
import org.xxpay.core.entity.SysUser;

import java.util.List;

@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/mch_info")
public class MchInfoController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /**
     * 查询商户信息
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        Long mchId = getValLongRequired("mchId");
        MchInfo dbRecord = rpc.rpcMchInfoService.getOneMch(mchId, null, getUser().getBelongInfoId());
        if (dbRecord == null) throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(dbRecord));
    }

    /** 新增商户信息 */
    @RequestMapping("/add")
    @MethodLog( remark = "新增商户" )
    public XxPayResponse add() {

        //获取请求参数
        MchInfo mchInfo = getObject(MchInfo.class);

        AgentInfo agentInfo = rpc.rpcAgentInfoService.findByAgentId(getUser().getBelongInfoId());

        mchInfo.setAgentId(agentInfo.getAgentId());
        mchInfo.setIsvId(agentInfo.getIsvId());
        mchInfo.setStatus(MchConstant.STATUS_AUDIT_ING); //商户状态： 代理商新增状态为： 待审核

        // 持久化操作
        rpc.rpcMchInfoService.add(mchInfo);

        return XxPayResponse.buildSuccess();
    }

    @RequestMapping("/list")
    public PageRes list() {

        MchInfo mchInfo = getObject( MchInfo.class);
        mchInfo.setAgentId(getUser().getBelongInfoId());

        IPage<MchInfo> result = rpc.rpcMchInfoService.selectPage(getIPage(), mchInfo);
        return PageRes.buildSuccess(result);
    }

    @RequestMapping("/all_list")
    public XxPayResponse allList() {
        MchInfo mchInfo = getObject(MchInfo.class);

        List<Long> allSubAgentIds = rpc.rpcAgentInfoService.queryAllSubAgentIds(getUser().getBelongInfoId());

        if(allSubAgentIds.isEmpty()){ //无任何子代理商
            return XxPayPageRes.buildSuccess(new JSONArray());
        }

        mchInfo.setPsVal("agentIdIn", allSubAgentIds);
        IPage<MchInfo> mchInfoList = rpc.rpcMchInfoService.selectPage(getIPage(true), mchInfo);
        JSONArray result = new JSONArray();
        for(MchInfo m : mchInfoList.getRecords()){
            JSONObject object = new JSONObject();
            object.put("mchId", m.getMchId());
            object.put("mchName", m.getMchName());
            result.add(object);
        }
        return XxPayPageRes.buildSuccess(result);
    }

    /** 重置登录密码 **/
    @MethodLog( remark = "重置商户登录密码" )
    @RequestMapping("/resetLoginPwd")
    public XxPayResponse resetLoginPwd() {

        Long mchId = getValLongRequired( "mchId");

        //判断越权操作
        MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null || !mchInfo.getAgentId().equals(getUser().getBelongInfoId())){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);
        }

        if(!rpc.rpcSysService.resetLoginPwd(MchConstant.INFO_TYPE_MCH, mchId)){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);
        }
        return XxPayResponse.buildSuccess();
    }

    /** 更新商户信息  */
    @RequestMapping("/update")
    @MethodLog( remark = "更新商户信息" )
    public ResponseEntity<?> update() {

        //获取请求参数并转换为对象类型
        MchInfo updateRecord = getObject(MchInfo.class);
        Long updatedMchId = updateRecord.getMchId();  //需更新的mchId

        MchInfo dbRecord = rpc.rpcMchInfoService.findByMchId(updatedMchId); //需更新的数据库信息
        if(dbRecord == null || !dbRecord.getAgentId().equals(getUser().getBelongInfoId())){ //越权操作
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }

        //当前商户状态， 非[审核失败]， 不允许更改商户信息
        if(dbRecord.getStatus() != MchConstant.STATUS_AUDIT_NOT){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }

        //更改状态为： 待审核
        updateRecord.setStatus(MchConstant.STATUS_AUDIT_ING);

        //更新操作
        rpc.rpcMchInfoService.updateMch(updateRecord);

        return ResponseEntity.ok(BizResponse.buildSuccess());

    }
}