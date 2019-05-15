package com.tracking.m2comsys.adapplication.utils;

import android.content.Context;
import android.util.Log;

import com.tracking.m2comsys.adapplication.BroadcastReceivers.BootService;
import com.tracking.m2comsys.adapplication.BroadcastReceivers.Google;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleTime implements Runnable {
    Google google;
    Context context;
//    public GoogleTime(Context context) {
//        this.context=context;
//    }


    public GoogleTime(Google google) {
        this.google = google;
    }

    public GoogleTime() {
    }

    @Override
    public void run() {

        try {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url("https://google.com/");
            Request request = builder.build();

            Response response = client.newCall(request).execute();
            Log.d("response", response.body().toString());
            String dateStr = response.header("Date");
            Log.d("response", dateStr);


            //Here I do something with the Date String
            System.out.println(dateStr);
            DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
            Date date = (Date) formatter.parse(dateStr);
            formatter.setTimeZone(TimeZone.getTimeZone("IST"));
            String date2 = formatter.format(date);
            System.out.println(date2);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
            String format = new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
            CommonDataArea.gdate = format;

            CommonDataArea.GoogleTime = date;
            System.out.println(date.getTime());
//                    Google google=new BootService();
//                    google.google(date);
//            google.google(date);

        } catch (ParseException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
