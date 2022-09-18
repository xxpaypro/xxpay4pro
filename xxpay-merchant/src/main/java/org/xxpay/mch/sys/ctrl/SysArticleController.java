package org.xxpay.mch.sys.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.SysArticle;
import org.xxpay.mch.common.ctrl.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/1/19
 * @description:
 */
@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/sys/article")
public class SysArticleController extends BaseController {

    /**
     * 查询文章
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        Long id = getValLongRequired( "id");
        SysArticle sysArticle = rpcCommonService.rpcSysArticleService.getById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(sysArticle));
    }

    /**
     * 新增文章
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增文章" )
    public ResponseEntity<?> add() {
        SysArticle sysArticle = getObject(SysArticle.class);

        if (sysArticle.getArticleType() == 1 || sysArticle.getArticleType() == 2) {
            SysArticle dbSysArticle = rpcCommonService.rpcSysArticleService.getOne(new LambdaQueryWrapper<SysArticle>()
                    .eq(SysArticle::getMchId, getUser().getBelongInfoId())
                    .eq(SysArticle::getArticleType, sysArticle.getArticleType())
            );
            if (dbSysArticle != null) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_TYPE_ARTICLE_EXISTS));
            }
        }

        sysArticle.setMchId(getUser().getBelongInfoId());

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(getUser().getBelongInfoId());
        sysArticle.setIsvId(mchInfo.getIsvId());

        boolean save = rpcCommonService.rpcSysArticleService.save(sysArticle);
        if(!save) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改系统消息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改文章" )
    public ResponseEntity<?> update() {
        SysArticle sysArticle = getObject(SysArticle.class);
        boolean update = rpcCommonService.rpcSysArticleService.updateById(sysArticle);
        if(!update) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 系统文章列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {
        SysArticle sysArticle = getObject(SysArticle.class);
        sysArticle.setMchId(getUser().getBelongInfoId());
        IPage<SysArticle> sysArticleList = rpcCommonService.rpcSysArticleService.list(sysArticle, getIPage());
        return ResponseEntity.ok(PageRes.buildSuccess(sysArticleList));
    }

    /**
     * 删除文章
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> delete(HttpServletRequest request) {
        String ids = getValStringRequired( "ids");
        String[] idss = ids.split(",");
        List<Long> _ids = new LinkedList<>();
        for(String id : idss) {
            if(NumberUtils.isDigits(id)) _ids.add(Long.parseLong(id));
        }
        rpcCommonService.rpcSysArticleService.batchDelete(_ids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}
