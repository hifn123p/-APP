package com.exemple.hifn123p.qiandao.Fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exemple.hifn123p.qiandao.R;


public class BottomBarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fg_bottom_bar_line, container, false);
        return view;
    }

}
