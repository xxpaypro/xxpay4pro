package org.xxpay.mch.shoppingMall.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchMiniVajra;
import org.xxpay.mch.common.ctrl.BaseController;

/**
 * @author: zx
 * @description: 会员APP金刚区
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mini_vajra")
public class MchMiniVajraController extends BaseController {

    /**
     * 轮播图配置列表
     * @return
     */
    @MethodLog( remark = "金刚区配置列表" )
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Byte status = getValByte("status");
        Byte authFrom = getValByte("authFrom");

        LambdaQueryWrapper<MchMiniVajra> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchMiniVajra::getMchId, getUser().getBelongInfoId());
        if (status != null) queryWrapper.eq(MchMiniVajra::getStatus, status);
        if (authFrom != null) queryWrapper.eq(MchMiniVajra::getAuthFrom, authFrom);
        queryWrapper.orderByAsc(MchMiniVajra::getSortNum);

        IPage<MchMiniVajra> vajraList = rpcCommonService.rpcMchMiniVajraService.page(getIPage(), queryWrapper);
        return ResponseEntity.ok(PageRes.buildSuccess(vajraList));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增金刚区" )
    public ResponseEntity<?> add() {

        MchMiniVajra mchMiniVajra = getObject(MchMiniVajra.class);
        mchMiniVajra.setMchId(getUser().getBelongInfoId());
        if (StringUtils.isBlank(mchMiniVajra.getVajraName())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STORE_BANNER_NULL));
        }
        boolean result = rpcCommonService.rpcMchMiniVajraService.save(mchMiniVajra);
        if(result){
            return ResponseEntity.ok(BizResponse.buildSuccess());
        }
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "编辑金刚区" )
    public ResponseEntity<?> update() {

        MchMiniVajra mchMiniVajra = getObject(MchMiniVajra.class);
        mchMiniVajra.setMchId(getUser().getBelongInfoId());
        if (StringUtils.isBlank(mchMiniVajra.getVajraName())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STORE_BANNER_NULL));
        }
        boolean result = rpcCommonService.rpcMchMiniVajraService.updateById(mchMiniVajra);
        if(result){
            return ResponseEntity.ok(BizResponse.buildSuccess());
        }
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 轮播图信息查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        Long vajraId = getValLong("vajraId");
        MchMiniVajra mchMiniVajra = rpcCommonService.rpcMchMiniVajraService.getOne(
                new QueryWrapper<MchMiniVajra>().lambda()
                        .eq(MchMiniVajra::getVajraId, vajraId)
                        .eq(MchMiniVajra::getMchId, getUser().getBelongInfoId())
        );
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchMiniVajra));
    }

    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除金刚区" )
    public ResponseEntity<?> delete() {
        // 获取轮播图ID
        String vajraId = getValString("vajraId");
        // 删除轮播图配置
        boolean removeBanner = rpcCommonService.rpcMchMiniVajraService.removeById(vajraId);
        if(removeBanner){
            return ResponseEntity.ok(BizResponse.buildSuccess());
        }
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }
}
