package com.exemple.hifn123p.qiandao.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.exemple.hifn123p.qiandao.R;

import com.exemple.hifn123p.qiandao.API.Post;

public class RegisterActivity extends Activity {
    private RadioButton nan=null;
    private RadioButton nu=null;
    private EditText inputName,inputTelNumber,inputPassword;
    String sexs,name,TelNumber,password;

    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_register);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar3);

        //设置沉浸  判断sdk版本 4.4以上
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        inputName = (EditText) findViewById(R.id.tx_xm);
        inputTelNumber=(EditText) findViewById(R.id.tx_dh);
        inputPassword = (EditText) findViewById(R.id.tx_pw);
        //性别
        RadioGroup sex = (RadioGroup) super.findViewById(R.id.xb);
        this.nan=(RadioButton)super.findViewById(R.id.nan);
        this.nu=(RadioButton)super.findViewById(R.id.nu);
        sex.setOnCheckedChangeListener(radiogpchange);

        //注册监听
        Button register = (Button) findViewById(R.id.button3);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    //性别选择
    private RadioGroup.OnCheckedChangeListener radiogpchange=new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId==nan.getId()){
                sexs  =  "男";
                Log.i("TAG",sexs);
            }
            else if(checkedId==nu.getId()){
                sexs =  "女";
                Log.i("TAG",sexs);
            }
        }
    };

    //注册函数
    public void register(){
        name = inputName.getText().toString();
        TelNumber = inputTelNumber.getText().toString();
        password = inputPassword.getText().toString();
        final String tag="register";
        final String data ="tag="+tag+"&sex="+sexs+"&name="+name+ "&telnumber=" +TelNumber + "&password=" + password;
        final Post pt=new Post();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message message = msgHandler.obtainMessage();
                    if(TelNumber.equals("")||password.equals("")) {
                        message.arg1=3;
                    }else if(TelNumber.length()!=11){
                        message.arg1=5;
                    }
                    else{
                        String result=pt.function_post(data);
                        switch (result) {
                            case "success":
                                message.arg1 = 1;
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case "exist":
                                message.arg1 = 2;
                                break;
                            case "empty":
                                message.arg1 = 3;
                                break;
                            default:
                                message.arg1 = 4;
                                break;
                        }
                    }
                    msgHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //重写Handler类
    private final Handler msgHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(RegisterActivity.this,"该号码已被注册",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(RegisterActivity.this,"电话或密码不能为空",Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(RegisterActivity.this,"请输入正确的电话",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}

