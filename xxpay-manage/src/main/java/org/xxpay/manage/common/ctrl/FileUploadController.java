package org.xxpay.manage.common.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.FileUtils;
import org.xxpay.manage.common.service.RpcCommonService;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/upload")
public class FileUploadController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /** 上传服务商证书 */
    @RequestMapping("/isv_cert")
    public XxPayResponse isvCert(@RequestParam("file") MultipartFile file) {

        if( file == null ) return XxPayResponse.build(RetEnum.RET_SERVICE_UPLOAD_FILE_NOT_EXISTS);

        try {

            //1. 待保存文件的名称
            String saveFileName = UUID.randomUUID() + FileUtils.getFileSuffix(file.getOriginalFilename(), true);

            //2. 保存文件
            String savePath = mainConfig.getUploadIsvCertRootDir() + File.separator + saveFileName;
            saveFile(file, savePath);

            //3. 返回响应地址
            return XxPayResponse.buildSuccess(saveFileName);

        } catch (Exception e) {
            logger.error("upload error, fileName = {}", file == null ? null :file.getOriginalFilename(), e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

}