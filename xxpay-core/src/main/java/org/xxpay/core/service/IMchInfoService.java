package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.SettBankAccount;
import org.xxpay.core.entity.SysUser;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/9/8
 * @description:
 */
public interface IMchInfoService extends IService<MchInfo> {

    /** 添加商户 **/
    void add(MchInfo mchInfo);

    /** 更新商户资料 **/
    void updateMch(MchInfo mchInfo);

    /** 审核商户 **/
    void auditMch(Long mchId, Byte auditStatus);

    int updateMch(MchInfo record, SettBankAccount mchSettBankAccount, SysUser mchUser);

    MchInfo getOneMch(Long mchId, Long isvId, Long agentId);

    MchInfo findByMchId(Long mchId);

    MchInfo findByLoginName(String loginName);

    MchInfo findByMobile(Long mobile);

    MchInfo findByEmail(String email);

    MchInfo findByUserName(String userName);

    IPage<MchInfo> selectPage(IPage page, MchInfo mchInfo);

    Integer count(MchInfo mchInfo);

    Map count4Mch();

    /** 统计商户数量：  根据代理商等级查询 **/
    Long countMchByAgentLevel(Date createTimeStart, Date createTimeEnd, Integer agentLevel, Long isvId, List<Long> agentIdList);

    /** 统计商户数量 **/
    Integer countAllMch(Date createTimeStart, Date createTimeEnd, Long isvId, boolean onlySubMch);

    /** 校验商户信息是否重复 **/
    void checkInfoRepeat(MchInfo record);

    /** 查询商户接口进件列表 **/
    List<MchInfo> selectSnashotPage(int offset, int limit, MchInfo mchInfo, Byte applyStatus, Byte applyType);

    /** 统计查询商户接口进件数量**/
    int countSnashot(MchInfo mchInfo, Byte applyStatus, Byte applyType);

    /**  通过商户ID查询对应的商户集合  **/
    List<Long> selectMchIdForNc(List<Long> mchIds);
}
