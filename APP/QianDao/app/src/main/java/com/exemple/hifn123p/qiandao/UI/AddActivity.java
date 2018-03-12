package com.exemple.hifn123p.qiandao.UI;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.exemple.hifn123p.qiandao.API.Post;
import com.exemple.hifn123p.qiandao.R;

public class AddActivity extends Activity {

    private EditText contents, ed_key;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);//引入主题
        setContentView(R.layout.activity_add);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar3);

        //判断sdk的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        contents = (EditText) findViewById(R.id.contents);
        ed_key = (EditText) findViewById(R.id.ed_key);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Button get_key = (Button) findViewById(R.id.get_key);
        ImageView left_back = (ImageView) findViewById(R.id.left_back);

        //左上角返回键
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //监听新建签到
        get_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetKey();
            }
        });
    }

    private void GetKey() {
        String inputContents = contents.getText().toString();
        String inputKey = ed_key.getText().toString();
        String telnumber=sp.getString("USER_NAME", "");
        final String tag = "add";
        final String data = "tag=" + tag + "&content=" + inputContents + "&mykey=" + inputKey+"&telnumber="+telnumber;
        System.out.println(data);
        final Post pt = new Post();

        if (!inputContents.equals("") && !inputKey.equals("")) {
            //在子线程中访问网络
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String result = pt.function_post(data);
                        Message message = handler.obtainMessage();
                        switch(result){
                            case "success":
                                message.arg1=1;
                                break;
                            case "exist":
                                message.arg1=2;
                                break;
                            default:
                                message.arg1=0;
                                break;
                        }
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            Message message1=handler.obtainMessage();
            message1.arg1=3;
            handler.sendMessage(message1);
        }
    }

    //重写Handler类
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Toast.makeText(AddActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(AddActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(AddActivity.this, "该口令已存在", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(AddActivity.this, "不能含有空项", Toast.LENGTH_SHORT).show();
            }
        }
    };
}