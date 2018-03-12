package com.exemple.hifn123p.qiandao.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.exemple.hifn123p.qiandao.R;
import com.exemple.hifn123p.qiandao.UI.GroupActivity;
import com.exemple.hifn123p.qiandao.UI.SearchActivity;
import com.exemple.hifn123p.qiandao.UI.SearchOthersActivity;

import java.util.Calendar;

public class SearchFragment extends Fragment {

    private String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_search, container, false);

        final EditText et_key=(EditText)view.findViewById(R.id.et_key);
        Button signin= (Button)view.findViewById(R.id.signin_search);
        Button all=(Button) view.findViewById(R.id.all_search);
        Button group=(Button) view.findViewById(R.id.group_search);
        DatePicker datePicker=(DatePicker) view.findViewById(R.id.dp);

        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            }

        });

        //成员考勤查询
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputKey=et_key.getText().toString();
                Intent intent=new Intent(getActivity(), SearchOthersActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("inputKey",inputKey);
                bundle.putString("date",date);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //考勤查询
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        //查询已加入签到组
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), GroupActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
