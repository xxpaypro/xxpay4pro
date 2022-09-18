package org.xxpay.mbr.ordering.ctrl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.entity.MchGoodsCategory;
import org.xxpay.mbr.common.ctrl.BaseController;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/mchGoods_category")
public class MchGoodsCategoryController extends BaseController {

    /**
     * 商品分类列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Byte authFrom = getValByteRequired("authFrom");

        MchGoodsCategory mchGoodsCategory = getObject(MchGoodsCategory.class);
        mchGoodsCategory.setMchId(getUser().getMchId());
        mchGoodsCategory.setAuthFrom(authFrom);
        IPage<MchGoodsCategory> mchGoodsCategoryList = rpcCommonService.rpcMchGoodsCategoryService.list(mchGoodsCategory, getIPage(true));
        return ResponseEntity.ok(PageRes.buildSuccess(mchGoodsCategoryList));
    }

}
