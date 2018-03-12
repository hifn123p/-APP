package com.exemple.hifn123p.qiandao.ZXING;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class ZXingApplication extends Application {

	public static final String TAG = "zxing_dzt";
	private static final boolean mIsShowLog = true;
	private static ZXingApplication instance;

	/**
	 * ȫ�ֵĴ�ӡ��Ϣ����
	 * 
	 * @param className
	 *            ����
	 * @param msg
	 *            Ҫ��ӡ����Ϣ
	 * @date 2014.07.28
	 */
	public static void print_i(String className, String msg) {
		if (mIsShowLog)
			Log.i(TAG, className + "---------->" + msg);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		System.gc();
	}

	public static Context getAppContext() {
		return instance;
	}
}
