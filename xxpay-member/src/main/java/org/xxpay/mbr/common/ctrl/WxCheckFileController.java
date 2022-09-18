package org.xxpay.mbr.common.ctrl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.mbr.common.service.RpcCommonService;

/** 微信文件验证目录 */
@RestController
public class WxCheckFileController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /**
     * 点餐小程序生成点餐码的验证规则，此处为校验微信验证文件接口
     * @param fileName
     * @return
     */
    @RequestMapping(MchConstant.MBR_QRCODE_CHECKFILE_PATH_FOOD + "/{mchId}/{authFrom}/{fileName}.txt")
    public ResponseEntity<?> getCheckFile(@PathVariable("mchId") Long mchId, @PathVariable("authFrom") Byte authFrom, @PathVariable("fileName") String fileName) {
        try {

            //根据名称 查询 获取服务商的文件内容
            String textVal = rpc.rpcMchWxauthInfoService.getCheckVal(fileName + ".txt", mchId, authFrom);

            if(StringUtils.isEmpty(textVal)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.TEXT_PLAIN);  //文本格式
            return new ResponseEntity<>(textVal, httpHeaders, HttpStatus.OK);

        }catch (Exception e) {
            logger.error("error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}