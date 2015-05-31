package com.example.govindmaheshwari.propertysale;

/**
 * Created by Govind Maheshwari on 5/27/2015.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class JSONParser {
    static String response = null;
    public final static int GET = 1;
    public JSONParser() {
    }
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }
    public String makeServiceCall(String url, int method,List<NameValuePair> params) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }
}