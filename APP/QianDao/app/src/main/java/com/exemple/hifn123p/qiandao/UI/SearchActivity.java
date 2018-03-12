package com.exemple.hifn123p.qiandao.UI;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.exemple.hifn123p.qiandao.API.Post;
import com.exemple.hifn123p.qiandao.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends Activity {

    private static final String TAG = "ASYNC_TASK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_search);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar3);

        //判断sdk的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //左上角返回键
        ImageView left_back = (ImageView) findViewById(R.id.left_back);
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String telnumber=sp.getString("USER_NAME", "");
        String tag="search";
        String data="tag="+tag+"&telnumber="+telnumber;
        MyTask myTask = new MyTask();
        myTask.execute(data);

    }

    //用来实现子线程与UI线程之间的交互
    private class MyTask extends AsyncTask<String,Integer,String>{
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute(){
            Log.i(TAG,"onPreExecute() called");
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String...params){
            Log.i(TAG, "doInBackground(Params... params) called");
            try {
                Post pt=new Post();
                return pt.function_post(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer...progresses){
            Log.i(TAG, "onProgressUpdate(Progress... progresses) called");
        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute(Result result) called");
            if (result.equals("failed")) {
                Message message = handler.obtainMessage();
                message.arg1 = 0;
                handler.sendMessage(message);
            } else {
                try {
                    //绑定一个数据为空的适配器
                    ListView listview = (ListView) findViewById(R.id.listView);
                    //处理服务器端传来的JSON数据
                    JSONArray jsonArray = new JSONArray(result);
                    List<HashMap<String, String>> datas = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String contents = jsonObject.getString("Contents");
                        byte[] a = Base64.decode(contents, Base64.DEFAULT);
                        contents = new String(a, "UTF8");
                        String date = jsonObject.getString("Date");
                        String time = jsonObject.getString("Time");
                        int condition=jsonObject.getInt("Conditions");
                        String conditions;
                        switch (condition){
                           case 1:
                               conditions="签到";
                               break;
                            case 2:
                                conditions="签离";
                                break;
                            default:
                                conditions="未知";
                                break;
                        }
                        //HashMap用来存储键值对
                        HashMap<String, String> item = new HashMap<>();
                        item.put("date", date);
                        item.put("times", time);
                        item.put("contents", contents);
                        item.put("conditions",conditions);
                        datas.add(item);
                    }
                    //创建SimpleAdapter适配器将数据绑定到item显示控件上
                    SimpleAdapter sim_adapter = new SimpleAdapter(SearchActivity.this, datas, R.layout.activity_search_item,
                            new String[]{"date", "times", "contents","conditions"}, new int[]{R.id.date, R.id.times, R.id.contents,R.id.condition});
                    //实现列表的显示
                    listview.setAdapter(sim_adapter);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
            Log.i(TAG, "onCancelled() called");
        }

    }

    //重写Handler类
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                Toast.makeText(SearchActivity.this, "未查到任何数据", Toast.LENGTH_SHORT).show();
            }
        }
    };

}

