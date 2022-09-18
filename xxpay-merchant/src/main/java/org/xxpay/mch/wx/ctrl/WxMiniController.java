package org.xxpay.mch.wx.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.MchGoods;
import org.xxpay.core.entity.MchMiniLive;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.wx.service.WxOpenMiniService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/wx_mini")
public class WxMiniController extends BaseController {

    private static final MyLog logger = MyLog.getLog(WxMiniController.class);

    @Autowired
    private WxOpenMiniService wxOpenMiniService;

    /**
     *  创建直播间
     * @return
     */
    @RequestMapping("/create_room")
    public XxPayResponse createRoom() {
        Long mchId = getUser().getBelongInfoId();
        MchMiniLive mchMiniLive = getObject(MchMiniLive.class);

        try {
            JSONObject respJSON = wxOpenMiniService.createRoom(mchId, mchMiniLive);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("请求失败：{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("操作失败：{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  获取直播间列表
     * @return
     */
    @RequestMapping("/get_live_info")
    public XxPayResponse getLiveInfo() {
        Long mchId = getUser().getBelongInfoId();
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.getliveinfo((getPageIndex() -1) * getPageSize(), getPageSize(), mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            JSONArray rooms = JSONArray.parseArray(respJSON.getString("room_info"));

            return XxPayPageRes.buildSuccess(rooms, respJSON.getInteger("total"));
        } catch (WxErrorException wxError) {
            logger.error("请求失败：{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("操作失败：{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  直播间导入商品
     * @return
     */
    @RequestMapping("/room_addgoods")
    public XxPayResponse roomAddGoods() {
        Long mchId = getUser().getBelongInfoId();
        Byte authFrom = getValByteRequired("authFrom");
        Long roomId = getValLongRequired("roomId");
        String[] ids = getValStringRequired("ids").split(",");
        List goodsIds = Arrays.asList(ids);

        try {
            JSONObject respJSON = wxOpenMiniService.roomAddGoods(mchId, authFrom, roomId, goodsIds);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayPageRes.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error(wxError, "请求失败");
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error(e, "操作失败");
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  获取商品列表
     * @return
     */
    @RequestMapping("/getapproved")
    public XxPayResponse getapproved() {
        Long mchId = getUser().getBelongInfoId();
        Byte authFrom = getValByteRequired("authFrom");
        Byte status = getValByteRequired("status");

        try {
            JSONObject respJSON = wxOpenMiniService.getapproved((getPageIndex() -1) * getPageSize(), getPageSize(), mchId, authFrom, status);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));
            JSONArray goods = respJSON.getJSONArray("goods");
            int total = respJSON.getInteger("total");

            return XxPayPageRes.buildSuccess(goods, total);
        } catch (WxErrorException wxError) {
            logger.error(wxError, "请求失败");
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error(e, "操作失败");
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  获取商品状态
     * @return
     */
    @RequestMapping("/goods_warehouse")
    public XxPayResponse goodsWareHouse() {
        Long mchId = getUser().getBelongInfoId();
        Byte authFrom = getValByteRequired("authFrom");
        String[] ids = getValStringRequired("ids").split(",");
        List goodsIds = Arrays.asList(ids);

        try {
            JSONObject respJSON = wxOpenMiniService.goodsWareHouse(mchId, authFrom, goodsIds);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));
            JSONArray goods = respJSON.getJSONArray("goods");
            int total = respJSON.getInteger("total");

            return XxPayPageRes.buildSuccess(goods, total);
        }catch (Exception e) {
            logger.error(e, "操作失败");
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * 更新商品
     * @return
     */
    @RequestMapping("/update_goods")
    public XxPayResponse updateGoods() {

        Long mchId = getUser().getBelongInfoId();
        Byte authFrom = getValByteRequired("industryType");
        Long goodsId = getValLongRequired("goodsId");
        String coverImgUrl = getValString("coverImgUrl");
        String name = getValString("name");//商品名称
        String priceType = getValString("priceType");//价格类型
        String price = getValString("price");
        String price2 = getValString("price2");
        String url = getValString("url");

        try {
            JSONObject reqParam = new JSONObject();
            reqParam.put("goodsId", goodsId);//微信端商品ID

            //商品信息
            if (StringUtils.isNotBlank("coverImgUrl")) reqParam.put("coverImgUrl", coverImgUrl);
            if (StringUtils.isNotBlank("name")) reqParam.put("name", name);
            if (StringUtils.isNotBlank("priceType")) reqParam.put("priceType", priceType);
            if (StringUtils.isNotBlank("price")) reqParam.put("price", price);
            if (StringUtils.isNotBlank(price2)) reqParam.put("price2", price2);
            /*String[] strArr = url.split("=");
            String goodsUrl = strArr[0] + "=" + URLEncoder.encode(strArr[1], "UTF-8");*/
            if (StringUtils.isNotBlank("url")) reqParam.put("url", url);
            logger.info("审核请求参数：{}", reqParam);

            JSONObject respJSON = wxOpenMiniService.updateGoods(mchId, authFrom, reqParam);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (Exception e) {
            logger.error(e, "操作失败");
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * 商品提交审核至小程序直播商品库
     * @return
     */
    @RequestMapping("/add_goods")
    public XxPayResponse addGoods() {

        Long mchId = getUser().getBelongInfoId();
        Byte authFrom = getValByteRequired("industryType");
        Long goodsId = getValLongRequired("goodsId");
        String coverImgUrl = getValStringRequired("coverImgUrl");
        String name = getValStringRequired("name");//商品名称
        String priceType = getValStringRequired("priceType");//价格类型
        String price = getValStringRequired("price");
        String price2 = getValString("price2");
        String url = getValStringRequired("url");

        try {
            JSONObject reqParam = new JSONObject();

            //商品信息
            reqParam.put("coverImgUrl", coverImgUrl);
            reqParam.put("name", name);
            reqParam.put("priceType", priceType);
            reqParam.put("price", price);
            if (StringUtils.isNotBlank(price2)) reqParam.put("price2", price2);
            /*String[] strArr = url.split("=");
            String goodsUrl = strArr[0] + "=" + URLEncoder.encode(strArr[1], "UTF-8");*/
            reqParam.put("url", url);
            logger.info("审核请求参数：{}", reqParam);

            JSONObject respJSON = wxOpenMiniService.addGoods(mchId, authFrom, goodsId, reqParam);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (Exception e) {
            logger.error(e, "操作失败");
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  撤回商品审核
     * @return
     */
    @RequestMapping("/reset_audit")
    public XxPayResponse resetAudit() {
        Long mchId = getUser().getBelongInfoId();
        Long goodsId = getValLongRequired("goodsId");

        MchGoods mchGoods = rpcCommonService.rpcMchGoodsService.getById(goodsId);
        if (mchGoods ==null) return XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);

        try {
            JSONObject respJSON = wxOpenMiniService.resetAudit(mchId, mchGoods.getIndustryType(), mchGoods.getMiniGoodsId(), mchGoods.getMiniAuditId());
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (Exception e) {
            logger.error(e, "操作失败");
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  重新提交商品审核
     * @return
     */
    @RequestMapping("/audit")
    public XxPayResponse audit() {
        Long mchId = getUser().getBelongInfoId();
        Long goodsId = getValLongRequired("goodsId");

        MchGoods mchGoods = rpcCommonService.rpcMchGoodsService.getById(goodsId);
        if (mchGoods ==null || mchGoods.getMiniGoodsId() == null) return XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);

        try {
            JSONObject respJSON = wxOpenMiniService.audit(mchId, mchGoods.getIndustryType(), mchGoods.getMiniGoodsId());
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (Exception e) {
            logger.error(e, "操作失败");
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  删除小程序直播库商品
     * @return
     */
    @RequestMapping("/delete")
    public XxPayResponse delete() {
        Long mchId = getUser().getBelongInfoId();
        Long goodsId = getValLongRequired("goodsId");//微信小程序商品库商品ID
        Byte authFrom = getValByteRequired("authFrom");//所属小程序

        try {
            JSONObject respJSON = wxOpenMiniService.delete(mchId, authFrom, goodsId);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (Exception e) {
            logger.error(e, "操作失败");
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 上传图片至微信服务器 */
    @RequestMapping("/upload")
    public XxPayResponse miniPic(@RequestParam("file") MultipartFile file) {

        Long mchId = getUser().getBelongInfoId();
        Byte authFrom = getValByteRequired("authFrom");

        if( file == null ) return XxPayResponse.build(RetEnum.RET_SERVICE_UPLOAD_FILE_NOT_EXISTS);

        try {
            //校验文件是否符合规范 & 获取文件后缀名
            String suffix = XXPayUtil.getImgSuffix(file.getOriginalFilename());
            String saveFileName = UUID.randomUUID() + "." + suffix;

            String savePath = mainConfig.getUploadMchStaticDir() + File.separator + MchConstant.MCH_IMG_SUB_DIR_WX_MINI_SCREENSHOT + File.separator + saveFileName;
            File saveFile = new File(savePath);

            //如果文件夹不存在则创建文件夹
            File dir = saveFile.getParentFile();
            if(!dir.exists()) dir.mkdirs();
            file.transferTo(saveFile);

            JSONObject respJSON = wxOpenMiniService.uploadMedia(mchId, authFrom, savePath);

            saveFile.delete();

            if (respJSON.getInteger("errcode")  != null && respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));
            return XxPayResponse.buildSuccess(respJSON);

        } catch (Exception e) {
            logger.error("upload error, fileName = {}", file == null ? null :file.getOriginalFilename(), e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * 将二进制转换成文件保存
     * @param instreams 二进制流
     * @param imgPath 图片的保存路径
     * @param imgName 图片的名称
     * @return
     *      1：保存正常
     *      0：保存失败
     */
    public static int saveToImgByInputStream(InputStream instreams,String imgPath,String imgName){
        int stateInt = 1;
        if(instreams != null){
            try {
                File file=new File(imgPath,imgName);//可以是任何图片格式.jpg,.png等
                FileOutputStream fos=new FileOutputStream(file);
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = instreams.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                stateInt = 0;
                e.printStackTrace();
            } finally {
            }
        }
        return stateInt;
    }



}