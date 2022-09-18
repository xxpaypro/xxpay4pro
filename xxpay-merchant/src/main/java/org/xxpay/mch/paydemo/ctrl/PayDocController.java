package org.xxpay.mch.paydemo.ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.mch.common.config.MainConfig;
import org.xxpay.mch.common.ctrl.BaseController;

@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/paydoc")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class PayDocController extends BaseController {

    private static final Logger _log = LoggerFactory.getLogger(PayDocController.class);
    
    @Autowired
    private MainConfig mainConfig;
    /**
     * 接口文档- 下载demo
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
    @RequestMapping(value = "/downloadDemo")
    @ResponseBody
    public ResponseEntity<?> downloadDemo() {
        
    	try {
    		_log.info("download ,userId={}, path={}",getUser().getBelongInfoId() , mainConfig.getDownloadDemoPath());
			File file = new File(mainConfig.getDownloadDemoPath());
			byte[] body = null;
			InputStream is = new FileInputStream(file);
			body = new byte[is.available()];
			is.read(body);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", file.getName());
			HttpStatus statusCode = HttpStatus.OK;
			ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
			is.close();
			return entity;
		} catch (FileNotFoundException e) {
			_log.error("FileNotFoundException error", e);
			return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
		} catch (Exception e) {
			_log.error("Exception error", e);
			return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
		}
    }

}
