package com.tracking.m2comsys.adapplication.utils;

import android.util.Log;

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

public class GoogleTime implements Runnable {
    @Override
    public void run() {
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet("https://google.com/"));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                String dateStr = response.getFirstHeader("Date").getValue();
                //Here I do something with the Date String
                System.out.println(dateStr);
                Log.d("googletime", dateStr);


                DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
                Date date = (Date)formatter.parse(dateStr);
                formatter.setTimeZone(TimeZone.getTimeZone("IST"));
                String date2=formatter.format(date);
                System.out.println(date2);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                String formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +cal.get(Calendar.YEAR);
                String format = new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
                CommonDataArea.gdate=format;

                CommonDataArea.GoogleTime=date;
                System.out.println(date.getTime());
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.d("Response", e.getMessage());
        } catch (IOException e) {
            Log.d("Response", e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
