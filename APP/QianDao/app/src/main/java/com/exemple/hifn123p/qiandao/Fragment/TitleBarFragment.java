package com.exemple.hifn123p.qiandao.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.exemple.hifn123p.qiandao.UI.MainActivity;
import com.exemple.hifn123p.qiandao.R;

public class TitleBarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fg_title_bar,container,false);

        Button exit = (Button) view.findViewById(R.id.id_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                sp.edit().putBoolean("AUTO_IS_CHECK", false).apply();//取消自动登录
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                startActivity(intent);//跳转回登录界面
                getActivity().finish();
            }
        });
        return view;
    }

}
