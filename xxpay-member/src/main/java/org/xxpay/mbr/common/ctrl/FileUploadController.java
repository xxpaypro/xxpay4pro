package org.xxpay.mbr.common.ctrl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
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
import org.xxpay.core.entity.Member;
import org.xxpay.mbr.common.service.RpcCommonService;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/upload")
public class FileUploadController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /** 会员头像上传 */
    @RequestMapping("/avatar")
    public XxPayResponse avatar(@RequestParam("file") MultipartFile file) {
        if( file == null ) return XxPayResponse.build(RetEnum.RET_SERVICE_UPLOAD_FILE_NOT_EXISTS);

        try {

            //1. 校验文件是否符合规范 & 获取文件后缀名
            String suffix = XXPayUtil.getImgSuffix(file.getOriginalFilename());
            String saveFileName = UUID.randomUUID() + "." + suffix;

            String fileUrl = commonSaveUpdFile(file, MchConstant.MBR_IMG_SUB_DIR_MEMBER_AVATAR, saveFileName);

            //3. 图片地址添加到会员
            Member member = getUser();
            member.setAvatar(fileUrl);
            boolean result = rpc.rpcMemberService.updateById(member);

            if (!result) return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
            return XxPayResponse.buildSuccess();

        } catch (Exception e) {
            logger.error("{} upload error, fileName = {}", MchConstant.MBR_IMG_SUB_DIR_MEMBER_AVATAR, file.getOriginalFilename(), e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 售后凭证上传 */
    @RequestMapping("/after_sale")
    public XxPayResponse afterSale(@RequestParam("file") MultipartFile file) {
        if( file == null ) return XxPayResponse.build(RetEnum.RET_SERVICE_UPLOAD_FILE_NOT_EXISTS);

        try {

            //1. 校验文件是否符合规范 & 获取文件后缀名
            String suffix = XXPayUtil.getImgSuffix(file.getOriginalFilename());
            String saveFileName = UUID.randomUUID() + "." + suffix;

            String fileUrl = commonSaveUpdFile(file, MchConstant.MBR_IMG_SUB_DIR_AFTER_SALE, saveFileName);

            return XxPayResponse.buildSuccess(fileUrl);

        } catch (Exception e) {
            logger.error("{} upload error, fileName = {}", MchConstant.MBR_IMG_SUB_DIR_MEMBER_AVATAR, file.getOriginalFilename(), e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 将上传的文件进行保存 - 公共函数 **/
    protected void saveFile(MultipartFile file, String savePath) throws Exception {

        File saveFile = new File(savePath);

        //如果文件夹不存在则创建文件夹
        File dir = saveFile.getParentFile();
        if(!dir.exists()) dir.mkdirs();
        file.transferTo(saveFile);
    }

    /** 保存上传的文件， 返回文件http下载地址， 仅上传 无需鉴权文件使用  **/
    protected String commonSaveUpdFile(MultipartFile file, String saveDir, String saveFileName) throws Exception{

        if(saveFileName == null) {  //如果名称为null, 使用UUID格式
            String suffix = FileUtils.getFileSuffix(file.getOriginalFilename(), false).toLowerCase();
            saveFileName = UUID.randomUUID() + "." + suffix;
        }

        //获取上传地址配置信息
        String uploadFileSaveType = this.rpc.rpcSysConfigService.getVal("uploadFileSaveType");
        if(MchConstant.UPDFILE_SAVE_LOCAL.equals(uploadFileSaveType)){ //上传文件存储位置： 本次

            //2. 保存文件
            String savePath = mainConfig.getUploadMchStaticDir() + File.separator + saveDir + File.separator + saveFileName;
            saveFile(file, savePath);

            //3. 返回响应地址
            return mainConfig.getUploadMchStaticViewUrl() + saveDir + "/" + saveFileName;


        }else if(MchConstant.UPDFILE_SAVE_ALIYUN_OSS.equals(uploadFileSaveType)){ //上传文件存储位置： 阿里云oss存储

            //oss配置信息参数
            Map<String, String> ossConfig = rpcCommonService.rpcSysConfigService.selectByCodes("ossEndpoint", "ossBucketName", "ossAccessKeyId", "ossAccessKeySecret");

            String ossBucketName = ossConfig.get("ossBucketName");
            String ossEndpoint = ossConfig.get("ossEndpoint");
            String objectName = saveDir + "/" + saveFileName;

            // 创建OSSClient实例
            OSS ossClient = new OSSClientBuilder().build(ossEndpoint, ossConfig.get("ossAccessKeyId"), ossConfig.get("ossAccessKeySecret"));

            // 上传文件。
            ossClient.putObject(ossBucketName, objectName, file.getInputStream());

            // 关闭OSSClient。
            ossClient.shutdown();

            return"https://" + ossBucketName + "." + ossEndpoint + "/" + objectName;
        }else{
            throw ServiceException.build(RetEnum.RET_SERVICE_UPLOAD_CONFIG_ERROR);
        }
    }


}