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

public class ApplyActivity extends Activity {

    private EditText ed_key;
    private String telnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);//引入主题
        setContentView(R.layout.activity_apply);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar3);

        //判断sdk的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        SharedPreferences sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        telnumber=sp.getString("USER_NAME","");
        ed_key=(EditText) findViewById(R.id.ed_key);
        Button bt_apply = (Button) findViewById(R.id.bt_apply);
        ImageView left_back = (ImageView) findViewById(R.id.left_back);

        //左上角返回键
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        bt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputKey = ed_key.getText().toString();
                Apply(telnumber,inputKey);
            }
        });
    }

    private void Apply(String telnumbers,String keys ){
        final String tag="apply";
        final String data="tag="+tag+"&telnumber="+telnumbers+"&keys="+keys;
        final Post pt=new Post();

        if (!keys.equals("")) {//口令不能为空
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Message message = handler.obtainMessage();
                        String result = pt.function_post(data);
                        switch (result) {
                            case "success":
                                message.arg1 = 1;
                                break;
                            case "exist":
                                message.arg1 = 2;
                                break;
                            case "error":
                                message.arg1=3;
                                break;
                            default:
                                message.arg1 = 4;
                                break;
                        }
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Message message2 = handler.obtainMessage();
            message2.arg1 = 5;
            handler.sendMessage(message2);
        }
    }

    private final Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.arg1){
                case 1:
                    Toast.makeText(ApplyActivity.this, "申请成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(ApplyActivity.this, "您已经申请过了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(ApplyActivity.this, "口令错误", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(ApplyActivity.this, "申请失败", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(ApplyActivity.this, "口令不能为空", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
