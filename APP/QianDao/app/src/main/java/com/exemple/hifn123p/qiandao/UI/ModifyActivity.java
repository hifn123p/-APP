package com.exemple.hifn123p.qiandao.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class ModifyActivity extends Activity {

    private EditText inputPassword, inputNewPassword, inputAgainPassword;
    String password, new_password, again_password;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_modify);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar3);

        //判断sdk的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        inputPassword = (EditText) findViewById(R.id.tx_ymm);
        inputNewPassword = (EditText) findViewById(R.id.tx_xmm);
        inputAgainPassword = (EditText) findViewById(R.id.tx_zcsr);
        Button modify = (Button) findViewById(R.id.button3);
        ImageView left_back = (ImageView) findViewById(R.id.left_back);

        //左上角返回键
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });
    }

    public void modify(){
        password = inputPassword.getText().toString();
        new_password = inputNewPassword.getText().toString();
        again_password = inputAgainPassword.getText().toString();
        sp=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = sp.getString("USER_NAME", "");
        final String tag="modify";
        final String data ="tag="+tag+ "&telnumber=" + username + "&password=" + password+"&newpassword="+new_password;
        final Post pt=new Post();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message message = msgHandler.obtainMessage();
                    if (!password.equals(new_password)) {
                        if (new_password.equals(again_password)) {
                            String result=pt.function_post(data);
                            switch(result){
                                case "success":
                                    message.arg1=1;
                                    break;
                                case "failed":
                                    message.arg1=4;
                                    break;
                            }
                        } else {
                            message.arg1=2;
                        }
                    } else {
                        message.arg1=3;
                    }
                    msgHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //重写Handler类
    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    Toast.makeText(ModifyActivity.this, "修改成功请重新登录", Toast.LENGTH_SHORT).show();
                    sp.edit().putBoolean("AUTO_IS_CHECK", false).apply();
                    Intent intent=new Intent(ModifyActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    Toast.makeText(ModifyActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(ModifyActivity.this, "新密码不能与旧密码相同", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(ModifyActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
