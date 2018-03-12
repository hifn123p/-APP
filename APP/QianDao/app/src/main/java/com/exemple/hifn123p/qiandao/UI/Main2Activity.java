package com.exemple.hifn123p.qiandao.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exemple.hifn123p.qiandao.Fragment.ManageFragment;
import com.exemple.hifn123p.qiandao.Fragment.SearchFragment;
import com.exemple.hifn123p.qiandao.Fragment.SignInFragment;
import com.exemple.hifn123p.qiandao.R;
import com.exemple.hifn123p.qiandao.SlidingMenu.SlidingMenu;


public class Main2Activity extends Activity {

    private SignInFragment mQiandao;
    private SearchFragment mChaxun;
    private ManageFragment mXiugai;
    private SlidingMenu slidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main2);

        //侧滑菜单
        ImageView imageView = (ImageView) findViewById(R.id.bt_sliding);
        slidingMenu = (SlidingMenu) findViewById(R.id.layout);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });


        // 初始化控件和声明事件
        LinearLayout mTabQiandao = (LinearLayout) findViewById(R.id.qiandao);
        LinearLayout mTabChaxun = (LinearLayout) findViewById(R.id.chaxun);
        LinearLayout mTabXiugai = (LinearLayout) findViewById(R.id.xiugai);


        View.OnClickListener handler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();

                switch (v.getId()) {
                    case R.id.qiandao:
                        if (mQiandao == null) {
                            mQiandao = new SignInFragment();
                        }
                        // 使用当前Fragment的布局替代id_content的控件
                        transaction.replace(R.id.id_fragment_content, mQiandao);
                        break;
                    case R.id.chaxun:
                        if (mChaxun == null) {
                            mChaxun = new SearchFragment();
                        }
                        transaction.replace(R.id.id_fragment_content, mChaxun);
                        break;
                    case R.id.xiugai:
                        if (mXiugai == null) {
                            mXiugai = new ManageFragment();
                        }
                        transaction.replace(R.id.id_fragment_content, mXiugai);
                        break;
                }
                // transaction.addToBackStack();
                // 事务提交
                transaction.commit();
            }
        };

        mTabQiandao.setOnClickListener(handler);
        mTabChaxun.setOnClickListener(handler);
        mTabXiugai.setOnClickListener(handler);

        //设置默认Fragment
        setDefaultFragment();

        //新建签到组
        TextView add = (TextView) findViewById(R.id.sliding_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        //加入签到组
        TextView apply=(TextView) findViewById(R.id.sliding_apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,ApplyActivity.class);
                startActivity(intent);
            }
        });

        //修改密码
        TextView modify=(TextView) findViewById(R.id.sliding_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, ModifyActivity.class);
                startActivity(intent);
            }
        });

    }

    // 设置默认Fragment的函数
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mQiandao = new SignInFragment();
        transaction.replace(R.id.id_fragment_content, mQiandao);
        transaction.commit();
    }

}


