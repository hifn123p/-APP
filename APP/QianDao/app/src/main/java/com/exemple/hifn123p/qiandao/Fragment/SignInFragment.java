package com.exemple.hifn123p.qiandao.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.exemple.hifn123p.qiandao.API.Post;
import com.exemple.hifn123p.qiandao.MyClass.Utils;
import com.exemple.hifn123p.qiandao.R;
import com.exemple.hifn123p.qiandao.ZXING.CaptureActivity;

public class SignInFragment extends Fragment {

    private EditText inputKey;
    private Button sign1;
    private SharedPreferences sp;
    private String telnumber, result, key, key2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fg_signin, container, false);

        sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        telnumber = sp.getString("USER_NAME", "");//电话
        String last_key = sp.getString("LAST_KEY", "");//上次用的口令
        inputKey = (EditText) view.findViewById(R.id.key);//口令
        sign1 = (Button) view.findViewById(R.id.sign1);//签到按钮
        Button cap= (Button) view.findViewById(R.id.cap);

        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CaptureActivity.class);
                startActivity(intent);
            }
        });

        //设置上次签到的口令
        inputKey.setText(last_key);
        key = inputKey.getText().toString();
        chaxun(key);

        //监听口令的变化
        inputKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                key2 = inputKey.getText().toString();
                chaxun(key2);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //判断是否已经到第二天
       /* SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));//确定时区（北京时间）
        Date curDate = new Date(System.currentTimeMillis());
        now_Date = formatter.format(curDate);
        if (!now_Date.equals(sp.getString("NOW_DATE", ""))) {
            sign1.setText("签到");
        }*/

        //监听签到、签离按钮
        sign1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastDoubleClick()) {//防止用户连续点击签到按钮
                    return;
                }
                String condition1=sp.getString("condition","");
                SignIn(condition1);
            }
        });

        return view;
    }

    //状态查询函数
    public void chaxun(String key2) {
        final String tag = "chaxun";
        final String data1 = "tag=" + tag + "&key=" + key2 + "&telnumber=" + telnumber;
        final Post pt1 = new Post();

        if (!key2.equals("")) {//如果口令不为空就查询
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Message msg=handler1.obtainMessage();
                        String result1 = pt1.function_post(data1);
                        switch (result1){
                            case "签到":
                                sp.edit().putString("condition", "签离").apply();
                                msg.arg1=5;
                                break;
                            case "签离":
                                sp.edit().putString("condition", "签到").apply();
                                msg.arg1=5;
                                break;
                            case "failed":
                                msg.arg1=6;
                                break;
                            default:
                                break;
                        }
                        handler1.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            Message msg2 = handler1.obtainMessage();
            msg2.arg1 = 6;
            handler1.sendMessage(msg2);
        }
    }


    //签到、签离函数
    public void SignIn(String con) {
        key = inputKey.getText().toString();
        final String tag = "come_go";
        final String data2 = "tag=" + tag + "&key=" + key + "&telnumber=" + telnumber + "&condition=" + con;
        final Post pt2 = new Post();

        if (!key.equals("")) {//口令不能为空
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Message message = handler1.obtainMessage();
                        result = pt2.function_post(data2);
                        switch (result) {
                            case "failed":
                                message.arg1 = 1;
                                break;
                            case "error":
                                message.arg1 = 4;
                                break;
                            case "no":
                                message.arg1=7;
                                break;
                            case "success":
                                message.arg1 = 2;
                                break;
                            case "touch_again":
                                message.arg1=8;
                                break;
                            default:
                                message.arg1 = 1;
                                break;
                        }
                        handler1.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Message message2 = handler1.obtainMessage();
            message2.arg1 = 3;
            handler1.sendMessage(message2);
        }
    }


    //重写Handler类
    private final Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            String condition2=sp.getString("condition","");
            switch (msg.arg1) {
                case 1:
                    Toast.makeText(getActivity(), condition2 + "失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    chaxun(key);
                    Toast.makeText(getActivity(), condition2 + "成功", Toast.LENGTH_SHORT).show();
                    sp.edit().putString("LAST_KEY", key).apply();//保存上次签到的口令
                    break;
                case 3:
                    sign1.setClickable(true);
                    Toast.makeText(getActivity(), "口令不能为空", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getActivity(), "口令错误", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    sign1.setVisibility(View.VISIBLE);
                    sign1.setClickable(true);
                    sign1.setText(condition2);
                    break;
                case 6:
                    sign1.setVisibility(View.INVISIBLE);
                    sign1.setClickable(false);
                    break;
                case 7:
                    Toast.makeText(getActivity(), "您还未加入该签到组", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(getActivity(), "请勿连续点击", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
