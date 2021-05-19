package com.example.newsapp;

public class News {
    private String mtitle;
    private String murl;
    private String mpub_date;
    private String mdescreption;
    private String mnewsfrom;

    News(String title,String url,String date,String des,String from)
    {
        mtitle=title;
        murl=url;
        mpub_date=date;
        mdescreption=des;
        mnewsfrom=from;
    }

    public String getMdescreption() {
        return mdescreption;
    }

    public String getMtitle() {
        return mtitle;
    }

    public String getMnewsfrom() {
        return mnewsfrom;
    }

    public String getMpub_date() {
        return mpub_date;
    }

    public String getMurl() {
        return murl;
    }
}
