package org.xxpay.mch.utils.printerUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.xxpay.mch.utils.printerUtils.exception.JsonParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class JacksonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
         objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

        //空值不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //反序列化时，属性不存在的兼容处理
        objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //单引号处理
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    private JacksonUtils() {
    }

    public static final ObjectMapper getInstance() {
        return objectMapper;
    }

    public static String obj2json(Object obj) {

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonParseException();
        }
    }

    public static <T> T json2pojo(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            throw new JsonParseException();
        }
    }

    public static <T> T json2pojo(String jsonStr, JavaType javaType) {
        try {
            return objectMapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            throw new JsonParseException();
        }
    }

    @SuppressWarnings("unchecked")
	public static <T> Map<String, Object> json2map(String jsonStr) {
        try {
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            throw new JsonParseException();
        }
    }

    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) {
        Map<String, Map<String, Object>> map;
        try {
            map = objectMapper.readValue(jsonStr,
                    new TypeReference<Map<String, T>>() {
                    });
        } catch (IOException e) {
            throw new JsonParseException();
        }
        Map<String, T> result = new HashMap<String, T>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }

    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
        List<Map<String, Object>> list = null;
        try {
            list = objectMapper.readValue(jsonArrayStr,
                    new TypeReference<List<T>>() {
                    });
        } catch (IOException e) {
            throw new JsonParseException();
        }
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(map2pojo(map, clazz));
        }
        return result;
    }

    public static <T> T map2pojo(Map<?, ?> map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }
}