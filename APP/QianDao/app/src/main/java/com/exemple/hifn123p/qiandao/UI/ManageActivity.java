package com.exemple.hifn123p.qiandao.UI;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exemple.hifn123p.qiandao.API.Post;
import com.exemple.hifn123p.qiandao.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageActivity extends ListActivity implements View.OnClickListener {

    //private static final String TAG = "ASYNC_TASK";

    private List<String> op;
    private Map<Integer, String> isCheckedMap;
    private String telnumber;
    private String key;
    private List<Item> itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_manage);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar3);

        //判断sdk的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //左上角返回键
        ImageView left_back = (ImageView) findViewById(R.id.left_back);
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //操作按钮监听
        Button add = (Button) findViewById(R.id.add);
        Button clear = (Button) findViewById(R.id.clear);
        Button manager = (Button) findViewById(R.id.manager);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);
        manager.setOnClickListener(this);

        //获得传过来的口令
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            key = bundle.getString("inputKey");
        }

        Init();
    }

    //初始化
    private void Init() {
        //选中数据的初始化
        op = new ArrayList<>();

        SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        telnumber = sp.getString("USER_NAME", "");
        String tag = "apply_search";
        String data = "tag=" + tag + "&telnumber=" + telnumber + "&key=" + key;
        MyTask myTask = new MyTask();
        myTask.execute(data);
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < itemList.size(); i++) {
            if (isCheckedMap.containsKey(i)) {
                op.add(isCheckedMap.get(i));
            }
        }
        final String dates;
        switch (v.getId()) {
            case R.id.manager:
                dates = "tag=" + "power_change" +"&power="+5+ "&key=" + key + "&telnumber=" + telnumber + "&op=" + op;
                Operate(dates);
                break;
            case R.id.clear:
                dates = "tag=" + "delete"+ "&key=" + key + "&telnumber=" + telnumber + "&op=" + op;
                Delete(dates);
                break;
            case R.id.add:
                dates = "tag=" + "power_change" +"&power="+1+ "&key=" + key + "&telnumber=" + telnumber + "&op=" + op;
                Operate(dates);
                break;
        }
    }

    //适配器
    class DraftDailyAdapter extends BaseAdapter {

        public List<Item> list;
        private Context context;
        LayoutInflater inflater;

        public DraftDailyAdapter(Context context, List<Item> list) {
            super();
            this.list = list;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            Item item = list.get(position);


            //这个用于记录item的id 操作ischeckmap来更新checkbox的状态
            final int id = item.id;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.activity_manage_item, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.manager_name);
                holder.group = (TextView) convertView.findViewById(R.id.manager_group);
                holder.attribute = (TextView) convertView.findViewById(R.id.manager_attribute);
                holder.cBox = (CheckBox) convertView.findViewById(R.id.manager_cb);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //设置listview
            holder.cBox.setTag(position);
            holder.tvName.setText(item.name);
            holder.group.setText(item.group);
            holder.attribute.setText(item.attribute);

            if (isCheckedMap.containsKey(position)) {
                holder.cBox.setChecked(true);
            } else {
                holder.cBox.setChecked(false);
            }

            addListener(holder, position);
            return convertView;
        }

        // checkbox的监听
        private void addListener(ViewHolder holder, final int position) {
            holder.cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!isCheckedMap.containsKey(buttonView.getTag()))
                            isCheckedMap.put((Integer) buttonView.getTag(), itemList.get(position).tel);
                    } else {
                        isCheckedMap.remove(buttonView.getTag());
                    }
                }
            });
        }

    }

    //显示列表累
    public final class ViewHolder {
        public TextView tvName;
        public TextView group;
        public TextView attribute;
        public CheckBox cBox;
    }

    //列表内容类
    class Item {
        private Integer id;
        private String name;
        private String group;
        private String attribute;
        private String tel;

        public String getName() {
            return name;
        }
    }

    //用来实现子线程与UI线程之间的交互
    private class MyTask extends AsyncTask<String, Integer, String> {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            //Log.i(TAG, "onPreExecute() called");
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params) {
            //Log.i(TAG, "doInBackground(Params... params) called");
            try {
                Post pt = new Post();
                return pt.function_post(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses) {
            //Log.i(TAG, "onProgressUpdate(Progress... progresses) called");
        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result) {
            //Log.i(TAG, "onPostExecute(Result result) called");
            if (result.equals("failed")) {
                Message message = handler.obtainMessage();
                message.arg1 = 1;
                handler.sendMessage(message);
            } else {
                try {
                    itemList = new ArrayList<>();
                    isCheckedMap = new HashMap<>();
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String contents = jsonObject.getString("FormName");
                        byte[] a = Base64.decode(contents, Base64.DEFAULT);
                        contents = new String(a, "UTF8");
                        String name = jsonObject.getString("Name");
                        byte[] b = Base64.decode(name, Base64.DEFAULT);
                        name = new String(b, "UTF8");
                        String tel = jsonObject.getString("Telnumber");
                        int power = jsonObject.getInt("Power");
                        String attribute;
                        switch (power) {
                            case 0:
                                attribute = "申请中";
                                break;
                            case 1:
                                attribute = "普通成员";
                                break;
                            case 5:
                                attribute = "管理员";
                                break;
                            case 9:
                                attribute = "组长";
                                break;
                            default:
                                attribute = "未知";
                                break;
                        }
                        Item item = new Item();
                        item.id = i;
                        item.name = name;
                        item.group = contents;
                        item.attribute = attribute;
                        item.tel = tel;
                        itemList.add(item);

                        //System.out.println(contents + "," + name + "," + attribute + "," + tel);
                        DraftDailyAdapter adapter = new DraftDailyAdapter(ManageActivity.this, itemList);
                        setListAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
            //Log.i(TAG, "onCancelled() called");
        }

    }

    //更改权限函数
    private void Operate(final String datas) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Post pt = new Post();
                    Message message = handler.obtainMessage();
                    String result = pt.function_post(datas);
                    switch(result){
                        case "failed":
                            message.arg1=2;
                            break;
                        case "no":
                            message.arg1=3;
                            break;
                        case "no data":
                            message.arg1=4;
                            break;
                        case "success":
                            message.arg1=5;
                            break;
                        default:
                            message.arg1=2;
                            break;
                    }
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //删除成员函数
    private void Delete(final String datas) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Post pt = new Post();
                    Message message = handler.obtainMessage();
                    String result = pt.function_post(datas);
                    switch(result){
                        case "failed":
                            message.arg1=2;
                            break;
                        case "no":
                            message.arg1=3;
                            break;
                        case "no data":
                            message.arg1=4;
                            break;
                        case "success":
                            message.arg1=5;
                            break;
                        default:
                            message.arg1=2;
                            break;
                    }
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //重写Handler类
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.arg1) {
                case 1:
                    Toast.makeText(ManageActivity.this, "未查到任何数据,可能是您没有权限查看", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Init();
                    Toast.makeText(ManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(ManageActivity.this, "您没有权限这么做", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(ManageActivity.this, "请勾选操作对象", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Init();
                    Toast.makeText(ManageActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}