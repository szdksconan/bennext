package com.unipay.benext.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/18.
 */
public class HttpUtil {

    /**
     * post请求接口
     * @param url 远程接口地址
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static String HttpPost(String url, Map params, int connecTimeout, int timeout)  throws Exception{
        String result = null;
        HttpPost request = new HttpPost(url);//这里发送get请求
        // 获取当前客户端对象
        HttpClient httpClient = new DefaultHttpClient();
        if(connecTimeout!=0&&timeout!=0){
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,connecTimeout);//连接时间
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,timeout);//数据传输时间
        }
        if (null != params) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for(Object key : params.keySet()) {
                if(params.get(key)!=null)
                    nvps.add(new BasicNameValuePair(key+"", params.get(key).toString()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
            request.setEntity(entity);
        }
        // 通过请求对象获取响应对象
        HttpResponse response = httpClient.execute(request);
        // 判断网络连接状态码是否正常(0--200都数正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(),"utf-8");
        }
        return result;
    }


    public static String HttpPost(String url, JSONObject obj) throws Exception {
        Map params = null;
        if (obj != null){
            params = new HashedMap();
            for (Object key : obj.keySet()){
                params.put(key+"", obj.get(key));
            }
        }
        url = url.indexOf("http://")==-1?("http://"+url):url;
        return HttpPost(url,params,0,0);
    }
}
