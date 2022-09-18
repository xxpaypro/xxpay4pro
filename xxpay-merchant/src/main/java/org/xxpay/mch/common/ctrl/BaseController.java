package org.xxpay.mch.common.ctrl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.ctrl.AbstractController;
import org.xxpay.core.common.util.FileUtils;
import org.xxpay.mch.common.config.MainConfig;
import org.xxpay.mch.common.service.RpcCommonService;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
@Controller
public abstract class BaseController extends AbstractController {

    @Autowired
    protected MainConfig mainConfig;
    
    @Autowired
    protected RpcCommonService rpcCommonService;

    /**
     * 输出excel数据
     * @param fileName
     * @param data
     */
    protected void writeExcelStream(String fileName, List<List> data) {

        try {
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String((fileName + ".xlsx").getBytes("gb2312"), "ISO8859-1"));//设置文件头编码格式
            response.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");//设置类型

            Workbook workbook = new XSSFWorkbook(); //生成excel 2007格式
            Sheet sheet = workbook.createSheet();

            for(int i = 0; i < data.size(); i++){
                Row row = sheet.createRow(i);

                for(int j = 0; j< data.get(i).size(); j++){
                    Cell cell = row.createCell(j);
                    if(data.get(i).get(j) != null) {
                        cell.setCellValue(data.get(i).get(j).toString());
                    }
                }
            }
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            logger.error("writeExcelStream", e);
        }
    }

    /** 保存上传的文件， 返回文件http下载地址， 仅上传 无需鉴权文件使用  **/
    protected String commonSaveUpdFile(MultipartFile file, String saveDir, String saveFileName) throws Exception{

        if(saveFileName == null) {  //如果名称为null, 使用UUID格式
            String suffix = FileUtils.getFileSuffix(file.getOriginalFilename(), false).toLowerCase();
            saveFileName = UUID.randomUUID() + "." + suffix;
        }

        //获取上传地址配置信息
        String uploadFileSaveType = this.rpcCommonService.rpcSysConfigService.getVal("uploadFileSaveType");
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
