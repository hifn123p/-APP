package com.exemple.hifn123p.qiandao.API;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Post{

    //public String url = "http://10.6.11.15/qiandao/qiandao.php";
    public String url = "http://39.181.34.10/android/qiandao/qiandao.php";

	public String function_post(String data)throws Exception {
        HttpURLConnection conn = null;
        InputStream in = null;
        BufferedReader reader = null;
        OutputStream out;
        try {
            URL myUrl = new URL(url);
            conn = (HttpURLConnection) myUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(3 * 1000);
            conn.setDoOutput(true);

            out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();
            conn.connect();
            int code = conn.getResponseCode();
            if (code == 200) {
                in = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                String state = reader.readLine();
                System.out.println("-------post:"+state);
                return state;            
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (in != null) {
                in.close();
            }
            assert conn != null;
            conn.disconnect();
        }
        return "failed";
    }

}