package com.exemple.hifn123p.qiandao.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.exemple.hifn123p.qiandao.API.Post;
import com.exemple.hifn123p.qiandao.R;


public class MainActivity extends Activity {
    private EditText et_userName, et_password;
    private CheckBox rem_pw, auto_login;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;


    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        //判断sdk的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //获取实例对象
        et_userName = (EditText) findViewById(R.id.user);
        et_password = (EditText) findViewById(R.id.password);
        rem_pw = (CheckBox) findViewById(R.id.cb1);
        auto_login = (CheckBox) findViewById(R.id.cb2);
        Button btn_login = (Button) findViewById(R.id.login_button);
        Button btn_register = (Button) findViewById(R.id.button2);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        ed = sp.edit();

        //判断记住密码多选框的状态
        if (sp.getBoolean("IS_CHECK", true)) {
            rem_pw.setChecked(true);
            et_userName.setText(sp.getString("USER_NAME", ""));
            et_password.setText(sp.getString("PASSWORD", ""));
            //判断自动登录多选框̬
            if (sp.getBoolean("AUTO_IS_CHECK", true)) {
                auto_login.setChecked(true);
                //跳转
                Intent intent=new Intent(MainActivity.this,WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                auto_login.setChecked(false);
            }
        }

        // 监听登录事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //监听记住密码多选框事件
        rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rem_pw.isChecked()) {
                    System.out.println("记住密码已选中");
                    ed.putBoolean("IS_CHECK", true).apply();
                } else {
                    System.out.println("记住密码没有选中");
                    ed.putBoolean("IS_CHECK", false).apply();
                }

            }
        });

        //监听自动登录多选框事件
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()) {
                    System.out.println("自动登录已选中");
                    ed.putBoolean("AUTO_IS_CHECK", true).apply();
                } else {
                    System.out.println("自动登录没有选中");
                    ed.putBoolean("AUTO_IS_CHECK", false).apply();
                }
            }
        });

        //监听注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    // 登录函数
    public void login() {
        final String userName = et_userName.getText().toString();
        final String password = et_password.getText().toString();
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
                        //使用此方法可不使用Handler通知主3线程，方法内所做操作由主线程完成
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = handler.obtainMessage();
                                if (isSuccess.equals("success")) {
                                    //记住用户名
                                    ed.putString("USER_NAME", userName);
                                    ed.commit();
                                    if (rem_pw.isChecked()) {
                                        //记住密码
                                        ed.putString("PASSWORD", password);
                                        ed.commit();
                                    }
                                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                                    MainActivity.this.startActivity(intent);
                                    finish();
                                } else {
                                    message.arg1 = 0;
                                    handler.sendMessage(message);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            Toast.makeText(MainActivity.this,"无网络连接",Toast.LENGTH_SHORT).show();
        }
    }

    //重写Handler类
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
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
