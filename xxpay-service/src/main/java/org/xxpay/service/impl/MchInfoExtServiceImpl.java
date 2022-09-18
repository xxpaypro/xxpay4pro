package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchInfoExt;
import org.xxpay.core.service.IMchInfoExtService;
import org.xxpay.core.service.IMchInfoService;
import org.xxpay.service.dao.mapper.MchInfoExtMapper;

/**
 * <p>
 * 商户扩展信息表（进件） 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-04
 */
@Service
public class MchInfoExtServiceImpl extends ServiceImpl<MchInfoExtMapper, MchInfoExt> implements IMchInfoExtService {

    @Autowired
    private IMchInfoService mchInfoService;

    @Override
    public MchInfoExt getOneMchExt(Long mchId, Long isvId, Long agentId){

        MchInfoExt record = new MchInfoExt();
        record.setMchId(mchId);
        record.setIsvId(isvId);
        record.setAgentId(agentId);
        LambdaQueryWrapper<MchInfoExt> queryWrapper = new LambdaQueryWrapper<>(record);
        return getOne(queryWrapper);
    }

    @Override
    public boolean saveOrUpdate(MchInfoExt record){
        boolean hasRecord = count(new LambdaQueryWrapper<MchInfoExt>().eq(MchInfoExt::getMchId, record.getMchId())) > 0;

        //验证法人身份证有效期 格式是否有误
        if(StringUtils.isNotEmpty(record.getLegalPersonIdCardDate()) && "0_0".equals(record.getLegalPersonIdCardDate())){
            throw new ServiceException(RetEnum.RET_SERVICE_IDCARD_DATE_ERROR);
        }

        //验证营业执照 格式是否有误
        if(StringUtils.isNotEmpty(record.getBusinessLicenseDate()) && "0_0".equals(record.getBusinessLicenseDate())){
            throw new ServiceException(RetEnum.RET_SERVICE_LICENSE_DATE_ERROR);
        }

        boolean updateFlag = false;
        if(hasRecord){
            updateFlag =  updateById(record);
        }else{
            updateFlag = save(record);
        }

        //更新失败
        if(!updateFlag) return updateFlag;

        //[待补充资料]  --》 [待签约]
        if("true".equals(record.getPsStringVal("isFinish"))){
            MchInfo updateRecord = new MchInfo();
            updateRecord.setSignStatus(MchConstant.MCH_SIGN_STATUS_WAIT_SIGN);
            return mchInfoService.update(updateRecord, new LambdaUpdateWrapper<MchInfo>()
                    .eq(MchInfo::getMchId, record.getMchId())
                    .eq(MchInfo::getSignStatus, MchConstant.MCH_SIGN_STATUS_WAIT_FILL_INFO)
            );
        }

        return updateFlag;
    }
}
