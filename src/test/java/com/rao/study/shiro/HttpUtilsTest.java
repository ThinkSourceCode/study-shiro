package com.rao.study.shiro;

import com.rao.study.shiro.utils.HttpUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpUtilsTest {

    //下面两个请求是分开的,如果需要session一致,即浏览器中的session一致

    @Test
    public void testLogin() throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("username","rao");
        params.put("password","123");
        String msg = HttpUtils.doPostByJSON("http://localhost:8085/myshiro/api/login",params,new HashMap<>());
        System.out.println(msg);
    }

    @Test
    public void test() throws Exception{
        Map<String,String> headsMap = new HashMap<>();
        headsMap.put("x-user-token","bf5c11addab32b47374d1bd5e0dbe5e0");
        String msg = HttpUtils.doPost("http://localhost:8085/myshiro/api/test2",new HashMap<>(),headsMap);
        System.out.println(msg);
    }

}
