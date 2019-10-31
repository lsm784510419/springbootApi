package com.fh.shop.api.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    public static String sendHttpClient(String url, Map<String,String> headers, Map<String,String> params){
        CloseableHttpClient client = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        String s = "";
        try {
            //打开浏览器
            client = HttpClientBuilder.create().build();
            //输入url地址
            httpPost = new HttpPost(url);
            //循环添加头信息，不为空才添加
            if (null != headers && headers.size()>0){
                //创建迭代器
                Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
                while (iterator.hasNext()){
                    //循环迭代器进行遍历
                    Map.Entry<String, String> next = iterator.next();
                    //添加头信息
                    httpPost.addHeader(next.getKey(),next.getValue());
                }
            }
            //循环添加参数信息    不为空，并且有值
            if (null != params && params.size()>0){
                //声明list集合放参数
                List<BasicNameValuePair> list = new ArrayList<>();
                    //用key循环获取每对key value
                Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()){
                    //循环遍历迭代器
                    Map.Entry<String, String> next = iterator.next();
                    String key = next.getKey();  //获取迭代器存入的key
                    String value = next.getValue();//获取迭代器存入的value
                    //放入list集合，因为url地址需要是一个list的参数
                    BasicNameValuePair basicNameValuePair = new BasicNameValuePair(key,value);
                    list.add(basicNameValuePair);
                }
                //转码
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list,"UTF-8");
                //放入post请求
                httpPost.setEntity(urlEncodedFormEntity);
            }
                //执行请求
            response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            s = EntityUtils.toString(entity,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpPost){
                httpPost.releaseConnection();
            }
            if (null != client){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return s;

    }
}
