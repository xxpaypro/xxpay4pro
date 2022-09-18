package org.xxpay.mbr.ordering.ctrl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchReceiveAddress;
import org.xxpay.mbr.common.ctrl.BaseController;

import java.util.List;

/**
 * <p>
 * 收货地址表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/mchReceiveAddress")
public class MchReceiveAddressController extends BaseController {

    /**
     * 收货地址列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        MchReceiveAddress mchReceiveAddress = new MchReceiveAddress();
        mchReceiveAddress.setMchId(getUser().getMchId());
        mchReceiveAddress.setMemberId(getUser().getMemberId());
        IPage<MchReceiveAddress> mchReceiveAddressList = rpcCommonService.rpcMchReceiveAddressService.list(mchReceiveAddress, getIPage(true));
        return ResponseEntity.ok(PageRes.buildSuccess(mchReceiveAddressList));
    }

    /**
     * 收货地址详情
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long addressId = getValLongRequired("addressId");
        MchReceiveAddress mchReceiveAddress = rpcCommonService.rpcMchReceiveAddressService.getOne(
                new QueryWrapper<MchReceiveAddress>().lambda()
                        .eq(MchReceiveAddress::getMemberId, getUser().getMemberId())
                        .eq(MchReceiveAddress::getAddressId, addressId)
        );

        return ResponseEntity.ok(PageRes.buildSuccess(mchReceiveAddress));
    }

    /**
     * 获取默认收货地址
     */
    @RequestMapping("/getDefaultAddress")
    @ResponseBody
    public ResponseEntity<?> getDefaultAddress() {

        LambdaQueryWrapper<MchReceiveAddress> queryWrapper = new QueryWrapper<MchReceiveAddress>().lambda();
        queryWrapper.eq(MchReceiveAddress::getIsDefaultAddress, MchConstant.PUB_YES);
        queryWrapper.eq(MchReceiveAddress::getMchId, getUser().getMchId());
        queryWrapper.eq(MchReceiveAddress::getMemberId, getUser().getMemberId());
        MchReceiveAddress defaultAddress = rpcCommonService.rpcMchReceiveAddressService.getOne(queryWrapper);
        if (defaultAddress != null) return ResponseEntity.ok(PageRes.buildSuccess(defaultAddress));

        LambdaQueryWrapper<MchReceiveAddress> queryWrapper2 = new QueryWrapper<MchReceiveAddress>().lambda();
        queryWrapper2.eq(MchReceiveAddress::getMchId, getUser().getMchId());
        queryWrapper2.eq(MchReceiveAddress::getMemberId, getUser().getMemberId());
        queryWrapper2.orderByDesc(MchReceiveAddress::getCreateTime);
        List<MchReceiveAddress> addressList = rpcCommonService.rpcMchReceiveAddressService.list(queryWrapper2);
        if (CollectionUtils.isNotEmpty(addressList)) {
            return ResponseEntity.ok(PageRes.buildSuccess(addressList.get(0)));
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 新增收货地址
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {

        MchReceiveAddress mchReceiveAddress = getObject(MchReceiveAddress.class);

        //设置商户ID、服务商ID和会员ID
        mchReceiveAddress.setMchId(getUser().getMchId());
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getMchId());
        mchReceiveAddress.setIsvId(mchInfo.getIsvId());
        mchReceiveAddress.setMemberId(getUser().getMemberId());

        //判断是否为默认收货地址
        if(mchReceiveAddress.getIsDefaultAddress() == MchConstant.PUB_YES){
            LambdaQueryWrapper<MchReceiveAddress> queryWrapper = new QueryWrapper<MchReceiveAddress>().lambda();
            queryWrapper.eq(MchReceiveAddress::getIsDefaultAddress, MchConstant.PUB_YES);
            queryWrapper.eq(MchReceiveAddress::getMchId, getUser().getMchId());
            queryWrapper.eq(MchReceiveAddress::getMemberId, getUser().getMemberId());
            MchReceiveAddress dbDefaultAddress = rpcCommonService.rpcMchReceiveAddressService.getOne(queryWrapper);
            //把当前默认收货地址改为非默认
            if(dbDefaultAddress != null){
                dbDefaultAddress.setIsDefaultAddress(MchConstant.PUB_NO);
                boolean updateResult = rpcCommonService.rpcMchReceiveAddressService.updateById(dbDefaultAddress);
                if(!updateResult) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
            }
        }

        //添加新的收货地址
        boolean saveResult = rpcCommonService.rpcMchReceiveAddressService.save(mchReceiveAddress);
        if(!saveResult) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 修改收货地址
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        MchReceiveAddress mchReceiveAddress = getObject(MchReceiveAddress.class);

        //判断是否为默认收货地址
        if(mchReceiveAddress.getIsDefaultAddress() == MchConstant.PUB_YES){
            LambdaQueryWrapper<MchReceiveAddress> queryWrapper = new QueryWrapper<MchReceiveAddress>().lambda();
            queryWrapper.eq(MchReceiveAddress::getIsDefaultAddress, MchConstant.PUB_YES);
            queryWrapper.eq(MchReceiveAddress::getMchId, getUser().getMchId());
            queryWrapper.eq(MchReceiveAddress::getMemberId, getUser().getMemberId());
            MchReceiveAddress dbDefaultAddress = rpcCommonService.rpcMchReceiveAddressService.getOne(queryWrapper);
            //把当前默认收货地址改为非默认
            if(dbDefaultAddress != null){
                dbDefaultAddress.setIsDefaultAddress(MchConstant.PUB_NO);
                boolean updateResult = rpcCommonService.rpcMchReceiveAddressService.updateById(dbDefaultAddress);
                if(!updateResult) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
            }
        }

        //更新收货地址
        boolean result = rpcCommonService.rpcMchReceiveAddressService.updateById(mchReceiveAddress);
        if(!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 删除收货地址
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> delete() {

        Long addressId = getValLongRequired("addressId");
        boolean result = rpcCommonService.rpcMchReceiveAddressService.remove(
                new QueryWrapper<MchReceiveAddress>().lambda()
                        .eq(MchReceiveAddress::getMemberId, getUser().getMemberId())
                        .eq(MchReceiveAddress::getAddressId, addressId)
        );

        if(!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(PageRes.buildSuccess());
    }


}
