package com.rao.study.shiro.utils;

import com.rao.study.shiro.domain.UserToken;
import com.rao.study.shiro.sql.SqlOperation;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private static final String X_USER_TOKEN = "X-USER-TOKEN";
    private static final String DEFAULT_ENCODING = "UTF-8";


    public static String getTokenFromRequest(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader(X_USER_TOKEN);
        if(StringUtils.isEmpty(token)){
            //如果为空,则从参数中获取
            token = httpServletRequest.getParameter(X_USER_TOKEN);
        }
        return token;
    }

    public static boolean isValidateToken(HttpServletRequest httpServletRequest){
        String token = getTokenFromRequest(httpServletRequest);
        if(StringUtils.isEmpty(token)){
            return false;
        }else{
            UserToken userToken = SqlOperation.getUserTokenByToken(token);
            if(userToken!=null){
                LocalDateTime expiredDate = userToken.getExpiredDate();
                if(LocalDateTime.now().isAfter(expiredDate)){//token已过期
                    return false;
                }else{
                    return true;
                }
            }else{
                return false;
            }
        }
    }


    public static String doPost(String url, Map<String,String> paramsMap) throws Exception {
        return doPost(url,paramsMap==null?new HashMap<>():paramsMap,new HashMap<>());
    }

    public static String doPost(String url,Map<String,String> paramsMap,Map<String,String> headersMap) throws Exception {

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        paramsMap.forEach((key,value)->{
            formparams.add(new BasicNameValuePair(key, value));
        });

        HttpEntity reqEntity = new UrlEncodedFormEntity(formparams, DEFAULT_ENCODING);

        return doPost(url,DEFAULT_ENCODING,reqEntity,headersMap==null?new HashMap<>():headersMap);
    }

    public static String doPostByJSON(String url,Map<String,String> paramsMap,Map<String,String> headersMap)throws Exception {
        HttpEntity reqEntity = new StringEntity(JsonUtils.toJSONString(paramsMap),DEFAULT_ENCODING);
        headersMap = headersMap==null?new HashMap<>():headersMap;
        headersMap.put("Content-type", ContentType.APPLICATION_JSON.getMimeType());
        return doPost(url,DEFAULT_ENCODING,reqEntity,headersMap);
    }

    public static String doPost(String url, String encoding, HttpEntity httpEntity,Map<String,String> headersMap) throws Exception{


        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)//一、连接超时：connectionTimeout-->指的是连接一个url的连接等待时间
                .setSocketTimeout(5000)// 二、读取数据超时：SocketTimeout-->指的是连接上一个url，获取response的返回等待时间
                .setConnectionRequestTimeout(5000)
                .build();
        List<Header> headers = new ArrayList<Header>();
        headersMap.forEach((key,value)->{
            Header header = new BasicHeader(key,value);
            headers.add(header);
        });

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setEntity(httpEntity);
        post.setConfig(requestConfig);
        post.setHeaders(headers.toArray(new Header[headers.size()]));
        HttpResponse response = client.execute(post);

        return doResponse(response,encoding);
    }

    public static String doGet(String url, Map<String,String> paramsMap) throws Exception{

        return doGet(url,DEFAULT_ENCODING,paramsMap==null?new HashMap<>():paramsMap,new HashMap<>());
    }

    public static String doGet(String url, Map<String,String> paramsMap,Map<String,String> headersMap) throws Exception{
        return doGet(url,DEFAULT_ENCODING,paramsMap==null?new HashMap<>():paramsMap,headersMap==null?new HashMap<>():headersMap);
    }

    public static String doGet(String url, String encoding, Map<String,String> paramsMap,Map<String,String> headersMap) throws Exception{

        URIBuilder uriBuilder = new URIBuilder(url);

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        paramsMap.forEach((key,value)->{
            formparams.add(new BasicNameValuePair(key, value));
        });

        uriBuilder.addParameters(formparams);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)//一、连接超时：connectionTimeout-->指的是连接一个url的连接等待时间
                .setSocketTimeout(5000)// 二、读取数据超时：SocketTimeout-->指的是连接上一个url，获取response的返回等待时间
                .setConnectionRequestTimeout(5000)
                .build();

        List<Header> headers = new ArrayList<Header>();
        headersMap.forEach((key,value)->{
            Header header = new BasicHeader(key,value);
            headers.add(header);
        });

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(uriBuilder.build());
        get.setConfig(requestConfig);
        get.setHeaders(headers.toArray(new Header[headers.size()]));
        HttpResponse response = client.execute(get);

        return doResponse(response,encoding);
    }

    public static String doResponse(HttpResponse response,String encoding) throws Exception{
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity resEntity = response.getEntity();
            String message = EntityUtils.toString(resEntity, encoding);
            System.out.println(message);
            return message;
        } else {
            System.out.println("请求失败");
            return null;
        }
    }


}
