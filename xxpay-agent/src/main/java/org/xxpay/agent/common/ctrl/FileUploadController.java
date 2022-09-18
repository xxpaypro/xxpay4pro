package org.xxpay.agent.common.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.XXPayUtil;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/upload")
public class FileUploadController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /** 上传商户进件相关资料 */
    @RequestMapping("/mch_apply")
    public XxPayResponse mchApply(@RequestParam("file") MultipartFile file) {

        if( file == null ) return XxPayResponse.build(RetEnum.RET_SERVICE_UPLOAD_FILE_NOT_EXISTS);

        try {

            //1. 校验文件是否符合规范 & 获取文件后缀名
            String suffix = XXPayUtil.getImgSuffix(file.getOriginalFilename());
            String saveFileName = UUID.randomUUID() + "." + suffix;

            //2. 保存文件
            String savePath = mainConfig.getUploadMchStaticDir() + File.separator + MchConstant.MCH_IMG_SUB_DIR_APPLY + File.separator + saveFileName;
            saveFile(file, savePath);

            //3. 返回响应地址
            String imgUrl = mainConfig.getUploadMchStaticViewUrl() + MchConstant.MCH_IMG_SUB_DIR_APPLY + "/" + saveFileName;
            return XxPayResponse.buildSuccess(imgUrl);

        } catch (Exception e) {
            logger.error("upload error, fileName = {}", file == null ? null :file.getOriginalFilename(), e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

}