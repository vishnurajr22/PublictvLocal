package com.tracking.m2comsys.adapplication.utils;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 01/27/2017.
 */

public class LogWriter {
    public static void writeLog(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {

                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/Log.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/Log.txt")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }


            if (file.length() > 500000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogRTC(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {

                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/RTCLog.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/RTCLog.txt")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }


            if (file.length() > 500000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeUncaughtLog(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {

                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/LogUncaughtExp.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/LogUncaughtExp.txt")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }


            if (file.length() > 500000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeLogCheckLive(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {

                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/CheckLive.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/CheckLive.txt")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }


            if (file.length() > 500000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogFirebase(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {

                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/FirebaseLog.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/FirebaseLog.txt")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }


            if (file.length() > 500000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogUSB(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/USBLog.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }


            if (file.length() > 1000000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogDownload(String content, String event)
    {
        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/DownloadLog.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }

            if (file.length() > 500000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogWifi(String content, String event)
    {
        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/WifiLog.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }

            if (file.length() > 1000000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogActivity(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/Activity.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }

            if (file.length() > 1000000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDailyPlayLog(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/DailyPlayLog.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }

            if (file.length() > 10000000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogPlay(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/PlayLog.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }

            if (file.length() > 1000000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeCSV(String content)
    {

        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/HistExport.csv";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }

            if (file.length() > 1000000) { //size of MB
                //delete the file
                file.delete();
            }


            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogPlayMethod(String content, String event)
    {
        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/PlayMethod.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }

            if (file.length() > 1000000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeLogPlayAlloc(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/PlayLogAlloc.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }

            if (file.length() > 1000000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogDailyPlayAlloc(String content, String event)
    {
        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/DailyPlayAlloc.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }

            if (file.length() > 1000000) { //size of MB
                //delete the file
                file.delete();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeLogException(String content, Exception exp)
    {
        try {
            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/PTV" + "/Exceptionlog.txt";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/PTV" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/PTV" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
                return;
            }
            if (file.length() > 500000) { //size of MB
                //delete the file
                file.delete();
            }

            StringBuilder report = new StringBuilder();
            Date curDate = new Date();
            report.append("----------Exception Handler collected Report--------------------------\r\n");
            report.append("Error Report collected on : ").append(curDate.toString()).append('\n').append('\n');
            report.append("Informations :").append('\n');
            report.append('\n').append('\n');
            report.append("Stack:\n");
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            exp.printStackTrace(printWriter);
            report.append(result.toString());
            printWriter.close();
            report.append('\n');
            report.append("**** End of current Report ***");

           // Utils.sendLogToServer("Critical Error "+exp.getMessage());
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : "+ report.toString() );
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
