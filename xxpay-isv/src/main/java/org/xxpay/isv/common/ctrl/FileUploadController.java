package org.xxpay.isv.common.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.FileUtils;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.isv.common.service.RpcCommonService;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/upload")
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


            String fileUrl = commonSaveUpdFile(file, MchConstant.MCH_IMG_SUB_DIR_APPLY, saveFileName);
            return XxPayResponse.buildSuccess(fileUrl);

        } catch (Exception e) {
            logger.error("upload error, fileName = {}", file == null ? null :file.getOriginalFilename(), e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 广告文件 */
    @RequestMapping("/ad")
    public XxPayResponse ad(@RequestParam("file") MultipartFile file) {

        if( file == null ) return XxPayResponse.build(RetEnum.RET_SERVICE_UPLOAD_FILE_NOT_EXISTS);

        try {

            String suffix = FileUtils.getFileSuffix(file.getOriginalFilename(), false).toLowerCase();

            //仅支持 图片 || mp4格式
            if(!MchConstant.ALLOW_UPLOAD_IMG_SUFFIX.contains(suffix) && !"mp4".equals(suffix) ){
                throw new ServiceException(RetEnum.RET_SERVICE_NOT_ALLOW_UPLOAD_IMG);
            }

            String fileUrl = commonSaveUpdFile(file, MchConstant.MCH_IMG_SUB_DIR_AD, null);
            return XxPayResponse.buildSuccess(fileUrl);

        } catch (Exception e) {
            logger.error("upload error, fileName = {}", file == null ? null :file.getOriginalFilename(), e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 上传服务商证书 */
    @RequestMapping("/cert")
    public XxPayResponse cert(@RequestParam("file") MultipartFile file) {

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


    /** 上传服务商第三方验证文件 (仅解析文件)  */
    @RequestMapping("/wx3rdCheckFile")
    public XxPayResponse wx3rdCheckFile(@RequestParam("file") MultipartFile file) {

        if( file == null ) return XxPayResponse.build(RetEnum.RET_SERVICE_UPLOAD_FILE_NOT_EXISTS);

        try {
            JSONObject result = new JSONObject();
            result.put("fileOriginName", file.getOriginalFilename());  //文件原始名称
            result.put("value", new String(file.getBytes()));  //文件内容
            return XxPayResponse.buildSuccess(result);

        } catch (Exception e) {
            logger.error("upload error, fileName = {}", file == null ? null :file.getOriginalFilename(), e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

}