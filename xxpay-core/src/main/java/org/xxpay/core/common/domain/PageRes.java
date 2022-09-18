package org.xxpay.core.common.domain;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.xxpay.core.common.constant.RetEnum;

/**
 * @Author terrfly
 * @Date 2019/1/23 23:19
 * @Description 为兼容layui分页插件 重新生成分页参数， layui不支持分页参数嵌套格式
 * 直接写： return ResponseEntity.ok(PageResponse.buildSuccess(IPage));
 * 返回结果为 {"code":0,"msg":"成功","data":{"records":[{我的数据}],"total":1,"size":10,"current":1,"pages":1}}
 * layui 配置返回字段时
 * ,response: {
 *                 ,countName: 'total' //规定数据总数的字段名称，默认：count
 *                 ,dataName: 'data' //规定数据列表的字段名称，默认：data
 *             }
 *  仅支持数据返回的直接参数，不支持多层嵌套参数 = = 、
 *  本类 将修改返回结果为： {"code":0,"msg":"成功","data":[{我的数据}],"total":1}
 **/
public class PageRes extends XxPayResponse {

    private Long count;

    public PageRes(int code, String msg, Object data) {
        super(code, msg, data);
    }

    public PageRes(RetEnum retEnum, Object data) {
        super(retEnum, data);
    }

    public static PageRes buildSuccess(IPage page) {

        PageRes res = new PageRes(RetEnum.RET_COMM_SUCCESS, page.getRecords());
        res.setCount(page.getTotal());
        return res;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "PageRes{" +
                "count=" + count +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
