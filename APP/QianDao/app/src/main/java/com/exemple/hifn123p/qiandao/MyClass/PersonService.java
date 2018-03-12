package com.exemple.hifn123p.qiandao.MyClass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PersonService {
    private static final String TAG="PersonService";

    private DataBaseOpenHelper dataBaseOpenHelper;
    private SQLiteDatabase database;

    public PersonService(Context context){
        dataBaseOpenHelper=new DataBaseOpenHelper(context);
    }

    public void save(String date,String contents,String times){
        database=dataBaseOpenHelper.getWritableDatabase();
        database.beginTransaction();
        try{
            database.execSQL("insert into person(Date,Contents,Times) values(?,?,?)",new String[]{date,contents,times});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        database.endTransaction();
    }

    public List<Person> getScrollData(int startResult, int maxResult) {
        List<Person> persons = new ArrayList<>();
        database = dataBaseOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from person limit ?,?",
                new String[] { String.valueOf(startResult), String.valueOf(maxResult) });
        while (cursor.moveToNext()) {
            persons.add(new Person(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }
        database.delete("person", "_id>?", new String[]{"0"});//清空数据库数据
        return persons;
    }

    /*public Cursor getCursorScrollData(int startResult, int maxResult) {
        database = dataBaseOpenHelper.getReadableDatabase();
        return database.rawQuery("select _id as _id,name,age from person limit ?,?",
                new String[] { String.valueOf(startResult), String.valueOf(maxResult) });
    }*/

}
