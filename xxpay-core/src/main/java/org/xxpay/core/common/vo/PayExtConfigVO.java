package org.xxpay.core.common.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author terrfly
 * @Date 2018/12/9 16:55
 * @Description 支付通道配置对象
 **/
public class PayExtConfigVO extends ExtConfigVO{

    public PayExtConfigVO(){}

    public PayExtConfigVO(String extConfig){

        JSONObject jsonObject = JSON.parseObject(extConfig);

        //获取visiblePassageList
        JSONArray visiblePassageList = jsonObject.getJSONArray("visiblePassageList");
        if(visiblePassageList != null){
            this.visiblePassageList = new ArrayList<>();
            for(Object idStr : visiblePassageList){
                this.visiblePassageList.add(ExtConfigVO.getRealId(idStr.toString()));
            }
        }

        //获取curPayPollParam
        JSONArray curPayPollParam = jsonObject.getJSONArray("curPayPollParam");
        if(curPayPollParam != null){
            this.curPayPollParam = new ArrayList<>();
            for(Object item : curPayPollParam){

                JSONObject jsonObjectForPoll = JSON.parseObject(item.toString());

                PollParam pollParam = new PollParam();
                //weight 默认为1
                pollParam.setWeight(jsonObjectForPoll.getInteger("weight") != null ?jsonObjectForPoll.getInteger("weight") : 1 );
                pollParam.setPayPassageId(ExtConfigVO.getRealId(jsonObjectForPoll.getString("payPassageId")));


                JSONArray jsonArrayForAccount = jsonObjectForPoll.getJSONArray("subAccountIdList");
                if(jsonArrayForAccount != null){
                    List<Integer> subAccountIdList = new ArrayList<>();
                    for(Object itemForAccount : jsonArrayForAccount){
                        subAccountIdList.add(ExtConfigVO.getRealId(itemForAccount.toString()));
                    }
                    pollParam.setSubAccountIdList(subAccountIdList);
                }

                this.curPayPollParam.add(pollParam);
            }
        }

    }


    private List<Integer> visiblePassageList;

    private List<PollParam> curPayPollParam;

    public List<Integer> getVisiblePassageList() {
        return visiblePassageList;
    }

    public void setVisiblePassageList(List<Integer> visiblePassageList) {
        this.visiblePassageList = visiblePassageList;
    }

    public List<PollParam> getCurPayPollParam() {
        return curPayPollParam;
    }

    public void setCurPayPollParam(List<PollParam> curPayPollParam) {
        this.curPayPollParam = curPayPollParam;
    }


    public class PollParam implements Serializable {

        private Integer payPassageId;
        private List<Integer> subAccountIdList;
        private Integer weight ;

        public Integer getPayPassageId() {
            return payPassageId;
        }

        public void setPayPassageId(Integer payPassageId) {
            this.payPassageId = payPassageId;
        }

        public List<Integer> getSubAccountIdList() {
            return subAccountIdList;
        }

        public void setSubAccountIdList(List<Integer> subAccountIdList) {
            this.subAccountIdList = subAccountIdList;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }
    }

}

