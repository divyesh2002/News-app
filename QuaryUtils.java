package com.example.newsapp;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QuaryUtils {
    private QuaryUtils()
    {
    }

    public static List<News> extractnews(String requesturl)
    {
        List<News> news=new ArrayList<>();
        URL url=createurl(requesturl);

        String jsonresponse="";
        try {
            jsonresponse=makehttprequest(url);
        } catch (IOException e) {

        }
        news=extractData(jsonresponse);
        return news;
    }

    private static URL createurl(String url)
    {
        URL makeurl=null;
        try {
            makeurl=new URL(url);
        } catch (MalformedURLException e) {
            //
        }
        return makeurl;
    }

    private static String makehttprequest(URL url) throws IOException
    {
        String jsonresponse="";
        if(url==null)
        {
            return jsonresponse;
        }
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;

        try{
            httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(20000);
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode()==200)
            {
                 inputStream=httpURLConnection.getInputStream();
                 jsonresponse=readInputstream(inputStream);
            }
        }
        catch(IOException e)
        {

        }finally {
            if(httpURLConnection!=null){
                 httpURLConnection.disconnect();
            }
            if(inputStream!=null)
            {
                inputStream.close();
            }
        }

        return jsonresponse;
    }

    private static String readInputstream(InputStream inputStream) throws IOException
    {
        StringBuilder builder=new StringBuilder();
        if(inputStream!=null) {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bf=new BufferedReader(inputStreamReader);
            String Line=bf.readLine();
            while(Line!=null) {
                builder.append(Line);
                Line = bf.readLine();
            }
        }
        return builder.toString();
    }

    private static List<News> extractData(String jsonresponse)
    {
        if(TextUtils.isEmpty(jsonresponse))
        {
            return null;
        }
        List<News> ans=new ArrayList<News>();

        try {
            JSONObject baseresponse=new JSONObject(jsonresponse);
            JSONArray  resultarray=baseresponse.getJSONArray("results");

            if(resultarray.length()>0)
            {
                for(int i=0;i<resultarray.length();i++)
                {
                     JSONObject currob=resultarray.getJSONObject(i);

                     String title=currob.getString("title");
                    String des=currob.getString("description");
                    String date=currob.getString("pubDate");
                    String from="From : "+currob.getString("source_id");
                    String url=currob.getString("link");
                    if(!des.equalsIgnoreCase("null")&&des.length()!=0) {
                        ans.add(new News(title, url, date, des, from));
                    }
                }
            }
        } catch (JSONException e) {
            //
        }
        return ans;
    }
}
