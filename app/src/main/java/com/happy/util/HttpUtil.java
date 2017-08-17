package com.happy.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 黄金云 on 2017/8/16.
 * 获取数据的工具
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){


        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn =  null;
                try {
                    URL url = new URL(address);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);

                    //获得在conn线路上的输入流对象
                    InputStream input = conn.getInputStream();
                    //获得在conn线路上的输入流读取对象
                    InputStreamReader streamReader = new InputStreamReader(input);
                    //从流中读取数据读取对象
                    BufferedReader reader = new BufferedReader(streamReader);

                    StringBuilder response = new StringBuilder();

                    String line ;
                    
                    while((line = reader.readLine()) != null){
                        response.append(line);

                    }

                    if(listener != null){
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    listener.onError(e);
                }finally{
                    if(conn != null){
                        //如果未断开与服务器的链接，进行断开操作
                        conn.disconnect();
                    }
                }

            }
        }).start();
    }



}
