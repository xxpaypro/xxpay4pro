package org.xxpay.isv.device;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.IsvDevice;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/isv_device")
public class IsvDeviceController extends BaseController {

    @Autowired
    private RpcCommonService rpc;


    /** 查询设备详情 */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        Long id = getValLongRequired("id");
        IsvDevice isvDevice = rpc.rpcIsvDeviceService.getOne(new QueryWrapper<IsvDevice>().lambda()
        .eq(IsvDevice::getId, id).eq(IsvDevice::getIsvId, getUser().getBelongInfoId()));

        return ResponseEntity.ok(XxPayResponse.buildSuccess(isvDevice));
    }


    @RequestMapping("/list")
    public PageRes list() {

        IsvDevice record = getObject(IsvDevice.class);
        record.setIsvId(getUser().getBelongInfoId());

        LambdaQueryWrapper<IsvDevice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IsvDevice::getIsvId, getUser().getBelongInfoId());

        if(StringUtils.isNotEmpty(record.getDeviceName())){
            queryWrapper.like(IsvDevice::getDeviceName, record.getDeviceName());
        }

        if(StringUtils.isNotEmpty(record.getDeviceNo())){
            queryWrapper.like(IsvDevice::getDeviceNo, record.getDeviceNo());
        }

        if(record.getDeviceType() != null ){
            queryWrapper.eq(IsvDevice::getDeviceType, record.getDeviceType());
        }

        if(record.getStatus() != null ){
            queryWrapper.eq(IsvDevice::getStatus, record.getStatus());
        }


        //查询结果
        IPage<IsvDevice> list = rpc.rpcIsvDeviceService.page(getIPage(), queryWrapper);

        /** 查询订单统计信息 **/
        List<String> deviceNoList = new ArrayList<>();
        list.getRecords().stream().forEach(d -> deviceNoList.add(d.getDeviceNo()));
        if(!deviceNoList.isEmpty()){  //不为空， 需查询订单统计信息
            Map<String, Long[]> orderCountMap = rpc.rpcMchTradeOrderService.selectByIsvDevice(getUser().getBelongInfoId(), deviceNoList);
            list.getRecords().stream().forEach(d ->{
                Long[] orderCountList = orderCountMap.get(d.getDeviceNo());
                if(orderCountList != null && orderCountList.length > 1){
                    d.setPsVal("orderCount", orderCountList[0]);
                    d.setPsVal("orderTotalAmount", orderCountList[1]);
                }
              }
            );
        }

        return PageRes.buildSuccess(list);
    }

    /** 新增商户信息 */
    @RequestMapping("/add")
    @MethodLog( remark = "新增设备" )
    public XxPayResponse add() {
        IsvDevice record = getObject(IsvDevice.class);

        Integer count = rpc.rpcIsvDeviceService.count(new QueryWrapper<IsvDevice>().lambda()
                .eq(IsvDevice::getDeviceType, record.getDeviceType())
                .eq(IsvDevice::getIsvId, getUser().getBelongInfoId())
                .eq(IsvDevice::getDeviceNo, record.getDeviceNo())
        );

        //设备编号已存在
        if(count > 0){
            throw ServiceException.build(RetEnum.RET_ISV_DEVICE_NO_EXISTS);
        }

        record.setIsvId(getUser().getBelongInfoId());
        if(rpc.rpcIsvDeviceService.save(record)){
            return XxPayResponse.buildSuccess();
        }

        return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
    }


    /** 更新设备信息  */
    @RequestMapping("/update")
    @MethodLog( remark = "更新设备信息" )
    public XxPayResponse update() {

        IsvDevice updateRecord = getObject(IsvDevice.class);

        Integer count = rpc.rpcIsvDeviceService.count(new QueryWrapper<IsvDevice>().lambda()
                .eq(IsvDevice::getDeviceType, updateRecord.getDeviceType())
                .eq(IsvDevice::getDeviceNo, updateRecord.getDeviceNo())
                .eq(IsvDevice::getIsvId, getUser().getBelongInfoId())
                .ne(IsvDevice::getId, updateRecord.getId())
        );

        //设备编号已存在
        if(count > 0){
            throw ServiceException.build(RetEnum.RET_ISV_DEVICE_NO_EXISTS);
        }

        boolean isTrue = rpc.rpcIsvDeviceService.update(updateRecord,
                new UpdateWrapper<IsvDevice>().lambda()
                .eq(IsvDevice::getId, updateRecord.getId())
                .eq(IsvDevice::getIsvId, getUser().getBelongInfoId()));


        if(isTrue){
            return XxPayResponse.buildSuccess();
        }
        return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
    }


    /** 删除设备 **/
    @MethodLog( remark = "删除设备" )
    @RequestMapping("/delete")
    public XxPayResponse delete() {

        Long id = getValLongRequired( "id");

        boolean isTrue = rpc.rpcIsvDeviceService.remove(new UpdateWrapper<IsvDevice>().lambda()
                .eq(IsvDevice::getId, id)
                .eq(IsvDevice::getIsvId, getUser().getBelongInfoId()));

        if(isTrue){
            return XxPayResponse.buildSuccess();
        }
        return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
    }
}