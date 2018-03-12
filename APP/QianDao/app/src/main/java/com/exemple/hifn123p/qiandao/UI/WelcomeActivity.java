package com.exemple.hifn123p.qiandao.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;

import com.exemple.hifn123p.qiandao.API.Post;
import com.exemple.hifn123p.qiandao.R;

public class WelcomeActivity extends Activity {

    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        new Handler().postDelayed(r, 2000);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            login();
        }
    };

    public void login() {
        final String userName = sp.getString("USER_NAME", "");
        final String password = sp.getString("PASSWORD", "");
        final String tag = "login";
        final String data = "tag=" + tag + "&telnumber=" + userName + "&password=" + password;
        final Post pt = new Post();

        if (isNetworkAvailable(this)) {
            //在子线程中访问网络
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String isSuccess = pt.function_post(data);
                        Message message = handler.obtainMessage();
                        if (isSuccess.equals("success")) {
                            Intent intent = new Intent(WelcomeActivity.this, Main2Activity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            message.arg1 = 0;
                            handler.sendMessage(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }).start();
        }else{
            Toast.makeText(WelcomeActivity.this,"无网络连接",Toast.LENGTH_SHORT).show();
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            sp.edit().putBoolean("AUTO_IS_CHECK", false).apply();//取消自动登录
            Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //重写Handler类
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                Toast.makeText(WelcomeActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                sp.edit().putBoolean("AUTO_IS_CHECK", false).apply();//取消自动登录
                startActivity(intent);
                finish();
            }
        }
    };

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}




