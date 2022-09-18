package org.xxpay.core.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class WeightRandom<K,V extends Number> {

    private TreeMap<Double, K> weightMap = new TreeMap<Double, K>();

    public WeightRandom(List<Pair<K, V>> list) {

        for (Pair<K, V> pair : list) {
            double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey().doubleValue();//统一转为double
            this.weightMap.put(pair.getValue().doubleValue() + lastWeight, pair.getKey());//权重累加
        }
    }
 
    public K random() {
        double randomWeight = this.weightMap.lastKey() * Math.random();
        SortedMap<Double, K> tailMap = this.weightMap.tailMap(randomWeight, false);
        return this.weightMap.get(tailMap.firstKey());
    }

    public static void main(String[] args) {

        List list = new LinkedList();
        Pair pair = new Pair("1111" , 100);
        list.add(pair);
        pair = new Pair("2222" , 1);
        list.add(pair);

        WeightRandom weightRandom = new WeightRandom(list);

        System.out.println(weightRandom.random());



        String str = "[{\"payPassageId\":1111,\"weight\":1},{\"payPassageId\":222,\"weight\":2}]";
        List pollPayPassList = new LinkedList();
        JSONArray array = JSONArray.parseArray(str);
        for(int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            int pid = object.getInteger("payPassageId");
            int weight = object.getInteger("weight");
            pair = new Pair(pid, weight);
            pollPayPassList.add(pair);

        }

        weightRandom = new WeightRandom(pollPayPassList);
        int pid = (Integer) weightRandom.random();
        System.out.println(pid);

        System.out.println(null instanceof String);

    }
 
}