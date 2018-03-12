package com.exemple.hifn123p.qiandao.MyClass;

public class Person {
    private String Date;
    private String Times;
    private String Contents;
    public Person(String date,String contents,String times){
        this.Date = date;
        this.Times = times;
        this.Contents = contents;
    }
    public String getDate(){
        return Date;
    }

    public String getTimes() {
        return Times;
    }

    public String getContents() {
        return Contents;
    }
}
