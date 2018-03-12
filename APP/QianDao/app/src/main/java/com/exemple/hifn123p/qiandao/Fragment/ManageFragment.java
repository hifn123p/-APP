package com.exemple.hifn123p.qiandao.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.exemple.hifn123p.qiandao.R;
import com.exemple.hifn123p.qiandao.UI.ManageActivity;


public class ManageFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fg_manage, container, false);

        final EditText et_key=(EditText)view.findViewById(R.id.et_key);
        Button apply= (Button)view.findViewById(R.id.apply_search);

        //成员管理
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputKey=et_key.getText().toString();
                Intent intent=new Intent(getActivity(), ManageActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("inputKey",inputKey);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

}
