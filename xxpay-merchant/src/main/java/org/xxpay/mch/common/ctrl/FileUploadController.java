package org.xxpay.mch.common.ctrl;

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
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.mch.common.service.RpcCommonService;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/upload")
public class FileUploadController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /** 操作员头像上传 */
    @RequestMapping("/avatar")
    public XxPayResponse avatar(@RequestParam("file") MultipartFile file) {
        return processFile(file, MchConstant.MCH_IMG_SUB_DIR_OPEARTOR_AVATAR);
    }

    /** 门店图片上传 */
    @RequestMapping("/store")
    public XxPayResponse store(@RequestParam("file") MultipartFile file) {
        return processFile(file, MchConstant.MCH_IMG_SUB_DIR_STORE);
    }

    /** 优惠券图片上传 */
    @RequestMapping("/coupon")
    public XxPayResponse coupon(@RequestParam("file") MultipartFile file) {
        return processFile(file, MchConstant.MCH_IMG_SUB_DIR_COUPON);
    }

    /** 积分商品图片上传 */
    @RequestMapping("/points")
    public XxPayResponse points(@RequestParam("file") MultipartFile file) {
        return processFile(file, MchConstant.MCH_IMG_SUB_DIR_POINTS);
    }

    /** 商品图片上传 */
    @RequestMapping("/goods")
    public XxPayResponse goods(@RequestParam("file") MultipartFile file) {
        return processFile(file, MchConstant.MCH_IMG_SUB_DIR_GOODS);
    }

    /** 小程序配置图片上传 */
    @RequestMapping("/mini")
    public XxPayResponse mini(@RequestParam("file") MultipartFile file) {
        return processFile(file, MchConstant.MCH_IMG_SUB_DIR_MINI);
    }

    /** 小程序可视化图片上传 */
    @RequestMapping("/visualable")
    public XxPayResponse visualable(@RequestParam("file") MultipartFile file) {
        return processFile(file, MchConstant.MCH_IMG_SUB_DIR_VISUALABLE);
    }

    /** 文档图片上传 */
    @RequestMapping("/article")
    public XxPayResponse article(@RequestParam("file") MultipartFile file) {
        return processFile(file, MchConstant.MBR_IMG_SUB_DIR_ARTICLE);
    }

    /** 封装通用函数 **/
    private XxPayResponse processFile(MultipartFile file, String mchImgSubDir){

        if( file == null ) return XxPayResponse.build(RetEnum.RET_SERVICE_UPLOAD_FILE_NOT_EXISTS);

        try {

            //1. 校验文件是否符合规范 & 获取文件后缀名
            String suffix = XXPayUtil.getImgSuffix(file.getOriginalFilename());
            String saveFileName = UUID.randomUUID() + "." + suffix;

            String imgUrl = commonSaveUpdFile(file, mchImgSubDir, saveFileName);
            return XxPayResponse.buildSuccess(imgUrl);

        } catch (ServiceException e) {
            return XxPayResponse.build(e.getRetEnum());
        } catch (Exception e) {
            logger.error("{} upload error, fileName = {}", mchImgSubDir, file.getOriginalFilename(), e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

}