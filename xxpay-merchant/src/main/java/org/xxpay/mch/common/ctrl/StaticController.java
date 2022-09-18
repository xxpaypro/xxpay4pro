package org.xxpay.mch.common.ctrl;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 静态文件下载/预览 ctrl
 * 1. 该地址不可被spring security 配置拦截 （目前该项目所有 [/* * /*.png],[/* * /*.jpg] 均放行 ，故上传需校验文件类型 ）
 * 2. 用户访问路径为： http://xxxx.org/imgs/store/123.png , 支持多级目录访问； 实际请求文件为：[store/123.png]
 * 3. 真实物理路径为： [商户上传路径 + 请求文件路径及名称] 例如： 商户上传路径为 /home/uer/upload , 请求文件为store/123.png = /home/uer/upload/store/123.png
 */
@Controller
public class StaticController extends BaseController {

    /** 图片预览 **/
    @GetMapping("/imgs/**/*.*")
    public ResponseEntity<?> imgView() {

        try {

            //1. 获取参数及配置信息
            String uploadMchStaticDir = mainConfig.getUploadMchStaticDir(); //商户上传文件路径
            String reqUri = request.getRequestURI();  //获取到请求路径 [/imgs/store/abc.png ]
            String dirAndFileName = reqUri.substring(reqUri.indexOf("/imgs/") + 6);  //截取路径，剩余路径及文件名称 [ store/123.png ]

            //2. 查找图片文件
            File imgFile = new File(uploadMchStaticDir + File.separator + dirAndFileName);
            if(!imgFile.isFile() || !imgFile.exists()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            //3. 输出文件流（图片格式）
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.IMAGE_JPEG);  //图片格式
            InputStream inputStream = new FileInputStream(imgFile);
            return new ResponseEntity<>(new InputStreamResource(inputStream), httpHeaders, HttpStatus.OK);

        } catch (FileNotFoundException e) {
            logger.error("static file error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /** 更新文件下载 **/
    @GetMapping("/update/download/*.*")
    public ResponseEntity<?> updateDownload() {

        try {
            //1. 获取参数及配置信息
            String uploadAppUploadDir = mainConfig.getUploadAppUpdateDir(); //更新文件 保存目录
            String reqUri = request.getRequestURI();  //获取到请求路径 [/update/download/abc.exe ]
            String fileName = reqUri.substring(reqUri.indexOf("/update/download/") + 17);  //截取路径，剩余路径及文件名称 [ 123.exe ]

            //2. 查找文件
            File downloadFile = new File(uploadAppUploadDir + File.separator + fileName);
            if(!downloadFile.isFile() || !downloadFile.exists()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            //3. 输出文件流
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", fileName); //文件名称
            InputStream inputStream = new FileInputStream(downloadFile);
            return new ResponseEntity<>(new InputStreamResource(inputStream), httpHeaders, HttpStatus.OK);

        } catch (FileNotFoundException e) {
            logger.error("static file error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
