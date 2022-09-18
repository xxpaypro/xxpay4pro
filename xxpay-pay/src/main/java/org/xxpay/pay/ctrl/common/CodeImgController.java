package org.xxpay.pay.ctrl.common;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.pay.util.CodeImgUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: dingzhiwei
 * @date: 17/12/27
 * @description:
 */
@Controller
public class CodeImgController extends BaseController {

    private static final MyLog _log = MyLog.getLog(CodeImgController.class);

    /**
     * 获取二维码图片流
     */
    @RequestMapping("/api/qrcode_img_get")
    public void getQrCodeImg(HttpServletRequest request, HttpServletResponse response) throws IOException, WriterException {
        JSONObject param = getJsonParam(request);
        String url = getStringRequired(param, "url");
        int width = getIntegerDefault(param, "width", 200);
        int height = getIntegerDefault(param, "height", 200);
        CodeImgUtil.writeQrCode(response.getOutputStream(), url, width, height);
    }

}
