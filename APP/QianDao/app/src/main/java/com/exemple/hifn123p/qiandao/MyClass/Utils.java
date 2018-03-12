package com.exemple.hifn123p.qiandao.MyClass;

/**
 * Created by Lee on 2015/11/5
 * 防止用户短时间内连续点击按钮
 */
public class Utils {
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
