package com.rao.study.shiro.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    public static String toJSONString(Object obj){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.writeValueAsString(obj);
        }catch (Exception e){
            return null;
        }
    }
}
