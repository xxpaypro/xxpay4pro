package org.xxpay.core.common.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.AgentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author terrfly
 * @Date 2018/12/9 16:55
 * @Description 代付通道配置对象
 **/
public class AgpayExtConfigVO extends ExtConfigVO{

    public AgpayExtConfigVO(){}

    public AgpayExtConfigVO(String extConfig){

        JSONObject jsonObject = JSON.parseObject(extConfig);

        //获取visiblePassageList
        JSONArray visibleAccList = jsonObject.getJSONArray("visibleAccList");
        if(visibleAccList != null){
            this.visibleAccList = new ArrayList<>();
            for(Object idStr : visibleAccList){
                this.visibleAccList.add(ExtConfigVO.getRealId(idStr.toString()));
            }
        }

        //获取curPayPollParam
        JSONArray curPayPollParam = jsonObject.getJSONArray("curPayPollParam");
        if(curPayPollParam != null){
            this.curPayPollParam = new ArrayList<>();
            for(Object ids : curPayPollParam){
                this.curPayPollParam.add(ExtConfigVO.getRealId(ids.toString()));
            }
        }
    }


    private List<Integer> visibleAccList;

    private List<Integer> curPayPollParam;


    public List<Integer> getVisibleAccList() {
        return visibleAccList;
    }

    public void setVisibleAccList(List<Integer> visibleAccList) {
        this.visibleAccList = visibleAccList;
    }

    public List<Integer> getCurPayPollParam() {
        return curPayPollParam;
    }

    public void setCurPayPollParam(List<Integer> curPayPollParam) {
        this.curPayPollParam = curPayPollParam;
    }
}
