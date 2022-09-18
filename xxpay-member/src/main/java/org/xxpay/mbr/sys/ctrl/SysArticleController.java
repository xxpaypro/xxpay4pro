package org.xxpay.mbr.sys.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.SysArticle;
import org.xxpay.mbr.common.ctrl.BaseController;

@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/sys_article")
@RestController
public class SysArticleController extends BaseController {

    private static final MyLog _log = MyLog.getLog(SysArticleController.class);

    /**
     * 文章详情
     * @return
     */
    @RequestMapping(value = "/get")
    @MethodLog( remark = "文章详情" )
    public ResponseEntity<?> get() {

        Byte articleType = getValByteRequired("articleType");
        Long mchId = getUser().getMchId();

        SysArticle sysArticle = rpcCommonService.rpcSysArticleService.getOne(new LambdaQueryWrapper<SysArticle>()
                .eq(SysArticle::getMchId, mchId)
                .eq(SysArticle::getArticleType, articleType)
        );
        return ResponseEntity.ok(XxPayResponse.buildSuccess(sysArticle));
    }



}
