package com.targetapps.despierta;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by JOKO on 15/04/2015.
 */

public class httpHandler extends Throwable {

    public String post(String posturl){

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost  = new HttpPost(posturl);

            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();

            String text = EntityUtils.toString(ent);
            return text;
        }catch (Exception e){
            return "error: "+e.getMessage();

        }

    }

    public String get(String geturl){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(geturl);

            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            String responseText = EntityUtils.toString(entity);
            return responseText;
        }catch (Exception e){return "ERROR";}

    }
}
