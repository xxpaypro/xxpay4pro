package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.vo.PayExtConfigVO;
import org.xxpay.core.entity.FeeRiskConfig;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.core.entity.FeeScaleExample;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.service.IAgentpayPassageService;
import org.xxpay.core.service.IFeeScaleService;
import org.xxpay.core.service.IPayPassageService;
import org.xxpay.service.dao.mapper.*;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FeeScaleServiceImpl extends ServiceImpl<FeeScaleMapper, FeeScale> implements IFeeScaleService {

    @Autowired
    private FeeScaleMapper feeScaleMapper;

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private AgentInfoMapper agentInfoMapper;

    @Autowired
    private AgentpayPassageMapper agentpayPassageMapper;

    @Autowired
    private PayPassageMapper payPassageMapper;

    @Autowired
    private IPayPassageService payPassageService;

    @Autowired
    private IAgentpayPassageService agentpayPassageService;

    @Autowired
    private FeeRiskConfigMapper feeRiskConfigMapper;

    @Autowired
    private PayOrderMapper payOrderMapper;





    void setCriteria(FeeScaleExample.Criteria criteria, FeeScale model) {
        if(model != null) {

            if(model.getStatus() != null && model.getStatus().byteValue() != -99) criteria.andStatusEqualTo(model.getStatus());
            if(model.getInfoId() != null) criteria.andInfoIdEqualTo(model.getInfoId());
            if(model.getInfoType() != null) criteria.andInfoTypeEqualTo(model.getInfoType());
            if(model.getProductType() != null) criteria.andProductTypeEqualTo(model.getProductType());
            if(model.getRefProductId() != null) criteria.andRefProductIdEqualTo(model.getRefProductId());
            if(model.getIsDefault() != null) criteria.andIsDefaultEqualTo(model.getIsDefault());

        }
    }

    @Override
    public FeeScale findById(Integer id) {
        return feeScaleMapper.selectByPrimaryKey(id);
    }

    @Override
    public FeeScale findOne(byte infoType, Long infoId, byte productType, Integer refProductId) {

        FeeScaleExample exa = new FeeScaleExample();
        exa.createCriteria().andInfoTypeEqualTo(infoType).andInfoIdEqualTo(infoId)
        .andProductTypeEqualTo(productType).andRefProductIdEqualTo(refProductId);
        List<FeeScale> result = feeScaleMapper.selectByExample(exa);
        return result == null || result.isEmpty() ? null : result.get(0);

    }

    public FeeScale getPayFeeByMch(Long mchId, Integer productId){
        return findOne(MchConstant.INFO_TYPE_MCH, mchId, MchConstant.FEE_SCALE_PTYPE_PAY, productId);
    }

    public FeeScale getPayFeeByAgent(Long agentId, Integer productId){
        return findOne(MchConstant.INFO_TYPE_AGENT, agentId, MchConstant.FEE_SCALE_PTYPE_PAY, productId);
    }

    public FeeScale getAgpayFeeByMch(Long mchId, Integer passageId){
        return findOne(MchConstant.INFO_TYPE_MCH, mchId, MchConstant.FEE_SCALE_PTYPE_AGPAY, passageId);
    }

    public FeeScale getAgpayFeeByAgent(Long agentId, Integer passageId){
        return findOne(MchConstant.INFO_TYPE_AGENT, agentId, MchConstant.FEE_SCALE_PTYPE_AGPAY, passageId);
    }

    public FeeScale getOffRechargeFeeByAgent(Long agentId){
        return findOne(MchConstant.INFO_TYPE_AGENT, agentId, MchConstant.FEE_SCALE_PTYPE_OFFRECHARGE, 0);
    }

    public FeeScale getOffRechargeFeeByMch(Long mchId){
        return findOne(MchConstant.INFO_TYPE_MCH, mchId, MchConstant.FEE_SCALE_PTYPE_OFFRECHARGE ,0);
    }

    @Override
    public FeeScale getPayFeeByIsv(Long isvId, Integer productId) {
        return findOne(MchConstant.INFO_TYPE_ISV, isvId, MchConstant.FEE_SCALE_PTYPE_PAY ,productId);
    }

    @Override
    public FeeScale selectAgpayAvailable(byte infoType, Long infoId) {
        return feeScaleMapper.selectAgpayAvailable(infoType, infoId);
    }

    @Override
    public List<FeeScale> productJoinFeeScale(byte infoType, Long infoId, String leftOrInner, boolean showExtInfo) {

        List<FeeScale> result = null;
        if("innerJoin".equals(leftOrInner)){
            result = feeScaleMapper.productInnerJoinFeeScale(infoType, infoId);
        }else if("leftJoin".equals(leftOrInner)){
            result = feeScaleMapper.productLeftJoinFeeScale(infoType, infoId);
        }

        if(showExtInfo) return result;
        for(FeeScale feeScale : result){
            feeScale.setExtConfig(null);
            feeScale.setPsVal("parentAgentFee", null);
        }
        return result;
    }

    @Override
    public List<FeeScale> agpayPassageJoinFeeScale(byte infoType, Long infoId, String leftOrInner, boolean showExtInfo){

        byte passageBelongInfoType = MchConstant.INFO_TYPE_PLAT;
        long passageBelongInfoId = 0L;

        List<FeeScale> result = null;

        if(infoType == MchConstant.INFO_TYPE_MCH){
            MchInfo mchInfo = mchInfoMapper.selectById(infoId);
            if(mchInfo.getType() == MchConstant.MCH_TYPE_PRIVATE){
                passageBelongInfoType = MchConstant.INFO_TYPE_MCH;
                passageBelongInfoId = infoId;
            }
        }

        if("innerJoin".equals(leftOrInner)){
            result = feeScaleMapper.agpayPassageInnerJoinFeeScale(infoType, infoId, passageBelongInfoType, passageBelongInfoId);
        }else if("leftJoin".equals(leftOrInner)){
            result = feeScaleMapper.agpayPassageLeftJoinFeeScale(infoType, infoId, passageBelongInfoType, passageBelongInfoId);
        }

        if(showExtInfo) return result;
        for(FeeScale feeScale : result){
            feeScale.setExtConfig(null);
            feeScale.setPsVal("parentAgentFee", null);
        }
        return result;

    }


    @Override
    public List<FeeScale> selectAll(byte infoType, Long infoId, byte productType) {
        FeeScaleExample example = new FeeScaleExample();
        example.setOrderByClause("createTime DESC");
        example.createCriteria().andInfoTypeEqualTo(infoType).andInfoIdEqualTo(infoId).andProductTypeEqualTo(productType);
        return feeScaleMapper.selectByExample(example);
    }


    @Override
    public List<FeeScale> select(int offset, int limit, FeeScale feeScale) {
        FeeScaleExample example = new FeeScaleExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        FeeScaleExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, feeScale);
        return feeScaleMapper.selectByExample(example);
    }

    @Override
    public Integer count(FeeScale feeScale) {
        FeeScaleExample example = new FeeScaleExample();
        FeeScaleExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, feeScale);
        return feeScaleMapper.countByExample(example);
    }


    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public Integer update(FeeScale feeScale) {

        checkFee(feeScale);

        //更新为非默认状态
        if(feeScale.getIsDefault() != null && feeScale.getIsDefault() == MchConstant.PUB_YES){
            this.updateNotDefault(feeScale.getInfoType(), feeScale.getInfoId(), feeScale.getProductType());
        }

        int updateRow = feeScaleMapper.updateByPrimaryKeySelective(feeScale);

        if(feeScale.getFeeRiskConfig() != null) {
            feeScale.getFeeRiskConfig().setFeeScaleId(feeScale.getId());
            feeRiskConfigMapper.updateByPrimaryKeySelective(feeScale.getFeeRiskConfig());
        }

        return updateRow;

    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public Integer add(FeeScale feeScale) {

        checkFee(feeScale);

        //更新为非默认状态
        if(feeScale.getIsDefault() != null && feeScale.getIsDefault() == MchConstant.PUB_YES){
            this.updateNotDefault(feeScale.getInfoType(), feeScale.getInfoId(), feeScale.getProductType());
        }

        int addRow = feeScaleMapper.insertSelective(feeScale);

        if(feeScale.getFeeRiskConfig() == null) {
            feeScale.setFeeRiskConfig(new FeeRiskConfig());
        }
        feeScale.getFeeRiskConfig().setFeeScaleId(feeScale.getId());
        feeRiskConfigMapper.insertSelective(feeScale.getFeeRiskConfig());

        return addRow;




    }

    private void checkFee(FeeScale feeScale){

        byte productType = feeScale.getProductType();
        Integer refProductId = feeScale.getRefProductId();
        byte infoType = feeScale.getInfoType();
        Long infoId = feeScale.getInfoId();

        BigDecimal currentSetFee = feeScale.getFee();

        Long parentAgentId = null;
        if(infoType == MchConstant.INFO_TYPE_MCH){

            MchInfo mchInfo = mchInfoMapper.selectById(infoId);
            if(mchInfo.getType() == MchConstant.MCH_TYPE_PRIVATE) return ; // 私有账户不校验，默认按运营平台的配置模板进行配置费用
            parentAgentId = mchInfo.getAgentId();
        }else{
            parentAgentId = agentInfoMapper.selectByPrimaryKey(infoId).getPid();
        }

        //如果当前角色存在上级代理商，判断： 当前设置费率  < 上级代理商费率
        if(parentAgentId != null && parentAgentId != 0){
            FeeScale parentAgentFee = this.findOne(MchConstant.INFO_TYPE_AGENT, parentAgentId, feeScale.getProductType(), feeScale.getRefProductId());
            if(parentAgentFee == null) throw new ServiceException(RetEnum.RET_MGR_MCH4AGENT_NOT_SET_PASSAGE);
            if(currentSetFee.compareTo(parentAgentFee.getFee()) < 0 ){
                throw new ServiceException(RetEnum.RET_MGR_SETFEE_LT_PARENT_AGENT_FEE);
            }
        }

        //判断： 当前设置费率 < 通道费率
        BigDecimal channelFee = null;

        if(MchConstant.FEE_SCALE_PTYPE_PAY == productType){ //支付
            channelFee = payPassageService.getMaxChannelFee((PayExtConfigVO) feeScale.getExtConfigVO()); //获取支付通道的最大费率
        }else if(MchConstant.FEE_SCALE_PTYPE_AGPAY == productType){  //代付
            channelFee = new BigDecimal(agentpayPassageMapper.selectByPrimaryKey(refProductId).getFeeEvery());
        }else if(MchConstant.FEE_SCALE_PTYPE_OFFRECHARGE == productType){ //线下充值
            channelFee = BigDecimal.ZERO;
        }

        if(currentSetFee.compareTo(channelFee) < 0 ){
            throw new ServiceException(RetEnum.RET_MGR_SETFEE_LT_CHANNEL_FEE);
        }


        if(infoType == MchConstant.INFO_TYPE_MCH) return ;

        //以下为代理商特有判断逻辑
        //当前设置费率 > 下级商户的最小费率
        BigDecimal childrenMinFeeByMch = feeScaleMapper.selectMinChildrenFee(MchConstant.INFO_TYPE_MCH, infoId, productType, refProductId);
        if(childrenMinFeeByMch != null && currentSetFee.compareTo(childrenMinFeeByMch) > 0){
            throw new ServiceException(RetEnum.RET_MGR_SETFEE_GT_CHILDREN_MCH_FEE);
        }

        //当前设置费率 > 下级代理商的最小费率
        BigDecimal childrenMinFeeByAgent = feeScaleMapper.selectMinChildrenFee(MchConstant.INFO_TYPE_AGENT, infoId, productType, refProductId);
        if(childrenMinFeeByAgent != null && currentSetFee.compareTo(childrenMinFeeByAgent) > 0){
            throw new ServiceException(RetEnum.RET_MGR_SETFEE_GT_CHILDREN_AGENT_FEE);
        }

        /// TODO 查找出删除通道信息，进行判断 1.是否存在商户设置过该通道， 2.是否下属代理商设置过该通道。
//        AgentAgentpayPassage record = agentAgentpayPassageMapper.selectByPrimaryKey(agentAgentpayPassage.getId());
//        String oldCanSetIds = record.getCanSetAccountId();
//        List<String> oldCanSetIdList = new ArrayList<String>();
//        if(StringUtils.isNotEmpty(oldCanSetIds)){
//            oldCanSetIdList = Arrays.asList(oldCanSetIds.split("##"));
//        }
//
//        String newCanSetIds = agentAgentpayPassage.getCanSetAccountId();
//        List<String> newCanSetIdList = new ArrayList<String>();
//        if(StringUtils.isNotEmpty(newCanSetIds)){
//            newCanSetIdList = Arrays.asList(newCanSetIds.split("##"));
//        }
//
//        ///查找出删除通道信息，进行判断 1.是否存在商户设置过该通道， 2.是否下属代理商设置过该通道。
//        for(String id : oldCanSetIdList){
//            if(!newCanSetIdList.contains(id)){
//                int count = mchAgentpayPassageMapper.countAllMchAccountIds4AllSubAgent(Integer.parseInt(id), agentAgentpayPassage.getAgentId());
//                if(count > 0 ){
//                    throw new ServiceException(RetEnum.RET_AGENT_DELETE_PASSAGE_ERROR_MCH);
//                }
//                count = agentAgentpayPassageMapper.countAllCanSetAccountIds4AllSubAgent(Integer.parseInt(id), agentAgentpayPassage.getAgentId());
//                if(count > 0 ){
//                    throw new ServiceException(RetEnum.RET_AGENT_DELETE_PASSAGE_ERROR_SUBAGENT);
//                }
//            }
//        }


    }


    @Override
    public Integer updateNotDefault(byte infoType, Long infoId, byte productType) {

        FeeScale updateRecord = new FeeScale();
        updateRecord.setIsDefault(MchConstant.PUB_NO);

        FeeScaleExample exa = new FeeScaleExample();
        exa.createCriteria().andInfoTypeEqualTo(infoType).andInfoIdEqualTo(infoId).andProductTypeEqualTo(productType);
        return feeScaleMapper.updateByExampleSelective(updateRecord, exa);
    }



    public void privateMchFeeConfig(List<FeeScale> feeScales){

        for(FeeScale record : feeScales){

            if(record.getId() == null){
                feeScaleMapper.insertSelective(record);
                continue;
            }

            FeeScale updateRecord = new FeeScale();
            updateRecord.setFee(record.getFee());
            updateRecord.setFeeScale(record.getFeeScale());
            updateRecord.setFeeScaleStep(record.getFeeScaleStep());
            updateRecord.setSingleFeeType(record.getSingleFeeType());

            FeeScaleExample exa = new FeeScaleExample();
            exa.createCriteria().andInfoIdEqualTo(record.getInfoId())
                    .andInfoTypeEqualTo(record.getInfoType()).andProductTypeEqualTo(record.getProductType());

            feeScaleMapper.updateByExampleSelective(updateRecord, exa); //将该私有账户下的所有计费方式进行更新。
        }
    }

    @Override
    public FeeScale selectInfoAndChannelFeeScale(byte infoType, Long infoId, byte feeScaleProductType, Integer refProductId, String bizOrderId){

        FeeScale feeScale = findOne(infoType, infoId, feeScaleProductType, refProductId);

        FeeScale channelFeeScale = new FeeScale();
        channelFeeScale.setFeeScale(MchConstant.FEE_SCALE_SINGLE);//单笔费用计算方式

        //设置平台渠道的费率 或费用
        if(feeScaleProductType == MchConstant.FEE_SCALE_PTYPE_PAY){   //支付业务

            channelFeeScale.setSingleFeeType(MchConstant.FEE_SCALE_SINGLE_RATE); //费率方式
            channelFeeScale.setFee(payOrderMapper.selectChannelRateByOrderId(bizOrderId));  //根据订单查询上游渠道费率

        }else if(feeScaleProductType == MchConstant.FEE_SCALE_PTYPE_AGPAY){   //代付业务

            channelFeeScale.setSingleFeeType(MchConstant.FEE_SCALE_SINGLE_FIX); //固定方式
            channelFeeScale.setFee(new BigDecimal(agentpayPassageMapper.selectByPrimaryKey(refProductId).getFeeEvery()));

        }else if(feeScaleProductType == MchConstant.FEE_SCALE_PTYPE_OFFRECHARGE){  //线下充值业务

            channelFeeScale.setSingleFeeType(MchConstant.FEE_SCALE_SINGLE_FIX);
            channelFeeScale.setFee(BigDecimal.ZERO);
        }

        feeScale.setPsVal("channelFeeScale", channelFeeScale);

        return feeScale;

    }

    @Override
    public Boolean updateMchFeeScale(FeeScale feeScale, FeeScale isvFeeScale) {
        //查询当前下级商户产品是否存在
        FeeScale selectById = feeScaleMapper.selectById(feeScale.getId());
        int result;
        if (selectById != null){
            result = feeScaleMapper.updateById(feeScale);
        }else {
            feeScale.setInfoType(MchConstant.INFO_TYPE_MCH);
            feeScale.setProductType(isvFeeScale.getProductType());
            feeScale.setRefProductId(isvFeeScale.getRefProductId());
            feeScale.setFeeScale(isvFeeScale.getFeeScale());
            feeScale.setFeeScaleStep(isvFeeScale.getFeeScaleStep());
            feeScale.setSingleFeeType(isvFeeScale.getSingleFeeType());
            feeScale.setExtConfig(isvFeeScale.getExtConfig());
            feeScale.setStatus(isvFeeScale.getStatus());
            feeScale.setIsDefault(isvFeeScale.getIsDefault());
            result = feeScaleMapper.insert(feeScale);
        }
        if (result != 1) return false;
        return true;
    }


}
