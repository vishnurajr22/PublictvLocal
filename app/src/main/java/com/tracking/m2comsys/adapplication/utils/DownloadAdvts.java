package com.tracking.m2comsys.adapplication.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.tracking.m2comsys.adapplication.Database.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by admin on 08/25/2017.
 */

public class DownloadAdvts implements Runnable {
    boolean succes = false;
    public static String fileName;
    public static String fileId;
    public static String videoId;
    public static String extn;
    public static int mapID;
    public static int isResumeExceed = 0;
    private VideoDetailsModel videoDetailsModel;
    private Context context;
    public DataBaseHelper dataBaseHelper;
    ArrayList<VideoDetailsModel> videoDetailsModels;
    public String downLoadingFileName;
    public static boolean isDownloading = false;
    public VideoDetailsModel detailsModel;

    public DownloadAdvts(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (true) {
            try {
                dataBaseHelper = CommonDataArea.dataBaseHelper;
                LogWriter.writeLogDownload("DownloadAdvts", "Fetching advt file list to download");
                if (!Utils.internetCheck()) {
                    if (!Utils.wifiCheck()) Utils.wifiReconnect();
                    try {
                        Thread.sleep(1000 * 10);
                    } catch (InterruptedException e) {
                        LogWriter.writeLogDownload("DownloadAdvts", "Internet not connected exit thread");
                        return;
                    }
                    continue;
                }
                String result = getResult();
                videoDetailsModels = new ArrayList<>();
                if (result != null) {
                    JSONArray jsonObjArray = new JSONArray(result);
                    if (jsonObjArray != null) {
                        if (jsonObjArray.length() > 0)
                            Utils.sendLogToServer("Download->Num advts to download->" + jsonObjArray.length(), context);
                        for (int i = 0; i < jsonObjArray.length(); i++) {
                            if (!isDownloading) {
                                LogWriter.writeLogDownload("DownloadAdvts", "Processsing down load list");
                                JSONObject object = jsonObjArray.getJSONObject(i);
                                long date = System.currentTimeMillis();

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String dateString = "NA";// sdf.format(date);
                                videoDetailsModel = new VideoDetailsModel();
                                fileId = object.getString("VL_DriveFileId");
                                fileName = object.getString("VL_VideoName");
                                Log.d("downloadadvts", "videp name inside loop " + fileName);
                                extn = object.getString("VL_FileMimeType");
                                videoId = object.getString("VL_VideoID");
                                int imp = Integer.parseInt(object.getString("VDL_Impressions"));
                                if (imp < 0) imp = 1;
                                String vdosz = object.getString("VL_Size");
                                Log.d("downloadadvts", "VLSIZE " + vdosz);
                                String endDate = object.getString("VDL_EndDate");
                                int vidMapID = object.getInt("VideoDeviceMapId");
                                int campaignId = object.getInt("CampaignId");
                                int totalImp = object.getInt("VDL_TotalImpression");
                                LogWriter.writeLogDownload("DownloadAdvts", "File to download VidID->" + videoId + ":" + fileId + "-" + fileName + "-Size:" + vdosz + "Imp:" + imp);
                                videoDetailsModel.setVideo_Name(fileName);
                                videoDetailsModel.setVideo_DriveId(fileId);
                                videoDetailsModel.setVideo_Extn(extn);
                                videoDetailsModel.setVideo_ServerId(videoId);
                                videoDetailsModel.setCampaignId(campaignId);
                                videoDetailsModel.setImpressions(imp);
                                videoDetailsModel.setSize(Integer.parseInt(vdosz));
                                videoDetailsModel.setVideo_DwlDate(dateString);
                                videoDetailsModel.setVidMapId(vidMapID);
                                videoDetailsModel.setTotalImpression(totalImp);
                                videoDetailsModel.setRemImpressions(totalImp);
                                videoDetailsModel.setEndDate(endDate);
                                videoDetailsModel.setStatusOfDays(dateString);

                                if (!dataBaseHelper.checkAlreadyDownloadedToday(videoDetailsModel.getVidMapId())) {
                                    videoDetailsModels.add(videoDetailsModel);
                                }
                            }

                            if (jsonObjArray.length() > 0) {
                                dataBaseHelper.check();
                                if (!isDownloading)
                                    downloadVideos(videoDetailsModels);
                            }
                        }

                    }
                }
            } catch (Exception e) {
                LogWriter.writeLog("Download Error", e.getMessage());
            }
            try {
                Thread.sleep(1000 * 20);
            } catch (InterruptedException e) {
                return;
            }
        }
    }


    private String getResult() {
        String chunks = "";
        String results = "";
        String url = "http://publictvads.in/WebServiceLiveTest/GetVideoList.php?id=" + CommonDataArea.uuid;
        Log.d("downloadadvts", "requesting  " + url);
        try {
            System.out.print("inside getString");
            URL url1 = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.setRequestMethod("GET");
            int statusCode = httpURLConnection.getResponseCode();
            if (statusCode == 200) {
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader reader1 = new BufferedReader(reader);
                StringBuilder builder = new StringBuilder();
                while ((chunks = reader1.readLine()) != null) {
                    builder.append(chunks);
                }
                results = builder.toString();
                Log.d("downloadadvts", "result of getVideoList " + results);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.print(e.toString());
            e.printStackTrace();
        }
        Log.d("downloadadvts", "get response" + results.toString());
        return results;
    }

    public void downloadVideos(ArrayList<VideoDetailsModel> detailsModels) {
        String lastPlayed;
        long date1 = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        lastPlayed = sdf.format(date1);

        for (int i = 0; i < detailsModels.size(); i++) {
            Log.d("downloadadvts", "inside download video");

            String url = "https://drive.google.com/uc?export=download&id=" + detailsModels.get(i).getVideo_DriveId();

            fileName = detailsModels.get(i).getVideo_Name();
            fileId = detailsModels.get(i).getVideo_DriveId();
            extn = detailsModels.get(i).getVideo_Extn();
            mapID = detailsModels.get(i).getVidMapId();

            int videoID = Integer.parseInt(detailsModels.get(i).getVideo_ServerId());

            String name = fileName + "." + extn;
            String path = Environment.getExternalStorageDirectory().toString() + "/advts" + "/" + name;
            File file = new File(path);
            if ((fileId == null) || (fileId.contains("null"))) {
                LogWriter.writeLogDownload("DownloadAdvts", "Can't download file Google Drive File ID null");
                Utils.sendLogToServer("Download-> Error file id null :" + fileName + "VideoID->" + videoID, context);
                //  updateAdvtDownloadSts(videoID, 0);
                continue;
            }
            DownloadTask downloadTask = new DownloadTask(CommonDataArea.mainActivity, detailsModels.get(i));
            dataBaseHelper.check();
            if (dataBaseHelper.checkAlreadyDownloaded(detailsModels.get(i).getVidMapId())) {
                LogWriter.writeLogDownload("DownloadAdvts", "File with same map Id already downloaded");
                Log.d("downloadadvts", "File with same map Id already downloaded");
                //  updateAdvtDownloadSts(videoID, 0);
                Utils.sendLogToServer("Download->Info Advt Campaign already downloaded" + String.valueOf(videoID), context);
                Log.d("downloadadvts", "Download Error Advt Campaign already downloaded" + String.valueOf(videoID));
                continue;
            }
            File fileExists = new File(CommonDataArea.rootFile, videoDetailsModel.getVideo_DriveId() + "." + videoDetailsModel.getVideo_Extn());
            /*file exist*/
            if (fileExists.exists()) {
                long existingFileSizez = fileExists.length();
                long actualSize = videoDetailsModel.getSize();
                Log.d("downloadadvts", "existingFileSizez " + existingFileSizez + " actualSize " + actualSize);
                /*size noe equal*/
                if (!(existingFileSizez == actualSize) && !isDownloading) {

                    File f = new File(CommonDataArea.rootFile + "/" + videoDetailsModel.getVideo_DriveId() + "." + videoDetailsModel.getVideo_Extn());
                    Boolean del = f.delete();
                    Log.d("viddel>>", "deleted>>" + CommonDataArea.rootFile + "/" + videoDetailsModel.getVideo_DriveId() + "." + videoDetailsModel.getVideo_Extn());
                    if (del) {
                        if (downloadTask.downLoadAFile(0)) {
                            downloadTask.onPostExecute(true);

                        } else downloadTask.onPostExecute(false);
                    }
                }
                /*size equal*/
                else {

                    detailsModel = videoDetailsModel;
                    downLoadingFileName = videoDetailsModel.getVideo_DriveId() + "." + videoDetailsModel.getVideo_Extn();
                    // downLoadingFileName=new DownloadTask().getDownLoadingFileName();
                    downloadTask.onPostExecute(true);


                }


            }
            /*file not exist*/
            else {

                if (downloadTask.downLoadAFile(0)) {
                    downloadTask.onPostExecute(true);
                } else downloadTask.onPostExecute(false);


            }


        }
    }

    private class DownloadTask {
        private Context context;
        String responsecode;

        public boolean isCancelled = false;

        public DownloadTask() {
        }


        public DownloadTask(Context context, VideoDetailsModel detailsModel) {
            this.context = context;


        }

        boolean isFilePresent() {
            String downLoadingFileName = detailsModel.getVideo_DriveId() + "." + detailsModel.getVideo_Extn();
            long fileSize = 0;
            File file = new File(CommonDataArea.rootFile, downLoadingFileName);
            if (file.exists()) fileSize = file.length();
            if ((fileSize > 0) && (fileSize == detailsModel.getSize()))
                return true; //file already exits
            else return false;
        }

        boolean isSpaceAvailable() {
            if (Utils.getAvailableSpace() > videoDetailsModel.getSize()) {
                LogWriter.writeLogDownload("Space check", "Space available for file->" + videoDetailsModel.getVideo_DriveId());
                return true;
            } else return false;
        }

        public boolean downLoadAFile(int isResume) {
            //todo move to settings
            isDownloading = true;
            String downloadUrl = "https://drive.google.com/uc?export=download&id=" + videoDetailsModel.getVideo_DriveId();
            Log.d("downloadadvts", "downloading from google " + downloadUrl);
            System.out.println(">> Downloading from video url = " + downloadUrl);
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            downLoadingFileName = videoDetailsModel.getVideo_DriveId() + "." + videoDetailsModel.getVideo_Extn();
            Log.d("downloadadvts", "downloading file name frpm downloadAFile " + downLoadingFileName);

            try {
//                if (isFilePresent()) {
//                    LogWriter.writeLogDownload("DownloadAdvts", "File already present in the folder. Just mark as downloaded");
//                    Utils.sendLogToServer("Download-> File already downloaded :" + detailsModel.getVideo_DriveId(), context);
//                    isDownloading = false;
//                    return true;
//                }
                if (!isSpaceAvailable()) {
                    Utils.sendLogToServer("Download-> Error No space in the disk- Trying to clean up", context);
                    if (!deleteForSpace(videoDetailsModel.getSize())) {
                        LogWriter.writeLogDownload("DownloadAdvts", "No space in disk to download advt");
                        Utils.sendLogToServer("Download->Error Failed to clean up for space.No space in the disk for file->" + detailsModel.getVideo_DriveId(), context);
                        isDownloading = false;
                        return false;
                    }
                }

                URL url = new URL(downloadUrl);
                connection = (HttpURLConnection) url.openConnection();
                File fileExists = new File(CommonDataArea.rootFile, videoDetailsModel.getVideo_DriveId() + "." + videoDetailsModel.getVideo_Extn());

                int exitLength = 0;


                if (isResume == 1) {
                    exitLength = (int) fileExists.length();
                    LogWriter.writeLogDownload("DownloadAdvts", "PartialDownloaded->" + exitLength);
                    Utils.sendLogToServer("Download->Parially downloaded requesting resume from ->" + exitLength, context);

                    connection.setRequestProperty("Range", "bytes=" + exitLength + "-");
                }

                connection.connect();
                connection.setConnectTimeout(60000 * 5);
                connection.setReadTimeout(60000 * 5);


                LogWriter.writeLogDownload("DownloadAdvts", "Length of file remaining->" + exitLength);
                Utils.sendLogToServer("Download->Start downloading file ->" + downLoadingFileName, context);

                input = connection.getInputStream();
                output = new FileOutputStream(new File(CommonDataArea.rootFile, downLoadingFileName), true);
                byte data[] = new byte[4096 * 4];
                long total = 0;
                int count;
                long fileSize = (long) videoDetailsModel.getSize();
                long lastUpdated = 0;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled) {
                        input.close();
                        isDownloading = false;
                        return false;
                    }
                    total += count;
                    lastUpdated += count;

                    output.write(data, 0, count);
                    if (lastUpdated > 500000) {
                        float percent = ((float) total / (float) fileSize) * 100f;
                        lastUpdated = 0;

                        Log.d("percentage", "per " + percent + " last updated " + lastUpdated);
                        Utils.sendLogToServer("Download->Progress percent -> " + percent + "Toatl ->" + total + "File Size ->" + fileSize, context);
                        ArrayList<String> camp = new ArrayList<String>();
                        String mapid = String.valueOf(videoDetailsModel.getVidMapId());

                        String progress = String.valueOf(percent);
                        camp.add("http://publictvads.in/modified/webservicelive/updatecampaignprogress.php?mapid=" + mapid + "&deviceid=" + CommonDataArea.uuid + "&campaignprogress=" + progress);
                        if (mapid != null && CommonDataArea.uuid != null && progress != null) {
                            DataBaseHelper dat = new DataBaseHelper(context);
                            Log.d("camp", String.valueOf(camp));
                            dat.new CampaignDetails().execute(camp);
                        }
                        succes = true;

                    }
                }
            } catch (Exception e) {
                isDownloading = true;
                LogWriter.writeLogDownload("DownloadAdvts Error exception", e.getMessage());
                Utils.sendLogToServer("Download-> Error exception :" + videoDetailsModel.getVideo_ServerId() + "-" + e.getMessage(), context);
                return false;
            } finally {
                try {
                    isDownloading = false;
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                    LogWriter.writeLogDownload("DownloadAdvts IO Exception", ignored.getMessage());
                    Utils.sendLogToServer("Download-> Error IO exception->" + videoDetailsModel.getVideo_DriveId() + "-" + ignored.getMessage(), context);
                }

                if (connection != null)
                    connection.disconnect();
                if (succes) {
                    ArrayList<String> camp = new ArrayList<String>();
                    String mapid = String.valueOf(videoDetailsModel.getVidMapId());
                    camp.add("http://publictvads.in/modified/webservicelive/updatecampaignprogress.php?mapid=" + mapid + "&deviceid=" + CommonDataArea.uuid + "&campaignprogress=" + 100);
                    DataBaseHelper dat = new DataBaseHelper(context);

                    dat.new CampaignDetails().execute(camp);
                }
            }
            return true;
        }


        public void onPostExecute(boolean success) {
            LogWriter.writeLogDownload("DownloadAdvts", "Update download status " + success);
            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(date);

            videoDetailsModel.setVideo_DwlDate(dateString);
            videoDetailsModel.setVideo_LastPlayed(dateString);
            Log.d("downloadadvts", "video name inside onPostExecute " + videoDetailsModel.getVideo_Name());
            int videoID = Integer.parseInt(videoDetailsModel.getVideo_ServerId());
            if (success) {
                int status = 1;
                try {
                    File file = new File(CommonDataArea.rootFile, downLoadingFileName);
                    long fileSize = file.length();
                    if (videoDetailsModel.getSize() == fileSize) {
                        status = 1;
                        videoDetailsModel.setStatus("1");
                    } else {
                        status = 0;
                        videoDetailsModel.setStatus("0");
                    }
                } catch (Exception exp) {
                    status = 0;
                }
                File fileExists = new File(CommonDataArea.rootFile, videoDetailsModel.getVideo_DriveId() + "." + videoDetailsModel.getVideo_Extn());
                if (fileExists.length() == videoDetailsModel.getSize()) {
                    //    Utils.sendLogToServer("Download->Completed successfully : updating status", context);
                    LogWriter.writeLogDownload("DownloadAdvts", "Mark doqnload success");
                    videoDetailsModel.setVideo_NoOfTimesPlayed(0);
                    Log.d("downloadadvts", "status " + status);
                    updateAdvtDownloadSts(videoID, status);
//                    dataBaseHelper.insertAdvtPlayEntry(videoDetailsModel);
                    ArrayList<String> camps = new ArrayList<String>();
                    String mapid = String.valueOf(videoDetailsModel.getVidMapId());


                    camps.add("http://publictvads.in/modified/webservicelive/updatecampaignprogress.php?mapid=" + videoDetailsModel.getVidMapId() + "&deviceid=" + CommonDataArea.uuid + "&campaignprogress=" + 100);
                    if (mapid != null && CommonDataArea.uuid != null) {
                        DataBaseHelper dat = new DataBaseHelper(context);
                        Log.d("camp", String.valueOf(camps));
                        dat.new CampaignDetails().execute(camps);
                    }
                    dataBaseHelper.insertAdvtPlayEntry(videoDetailsModel);
                }


            } else {
                Utils.sendLogToServer("Download->Failed :updating status", context);
                Log.d("downloadadvts", "Mark doqnload failed");
                LogWriter.writeLogDownload("DownloadAdvts", "Mark doqnload failed");
                videoDetailsModel.setStatus("0");
                //updateAdvtDownloadSts(videoID, 0);
            }
            isDownloading = false;
        }
    }

    boolean updateAdvtDownloadSts(int vidId, int status) {
        String text = "";
        BufferedReader reader = null;
        try {
            //  status = 1;//todo Server is not marking video as errored out so setting it as downloaded. Change it later
            String commenPath = "http://publictvads.in/WebServiceLiveTest/UpdateDownloadedVideoInfo.php?";
            String variablePath = "vid=" + vidId + "&did=" + CommonDataArea.uuid + "&res=" + status + "&vdlid=" + mapID;
            Log.d("downloadadvts", "status sent");
            String url = commenPath + variablePath;

            Log.d("downloadadvts", "updating video downloaded status" + url);
            try {
                LogWriter.writeLog("DownLoad", commenPath);
                URL uRl = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) uRl.openConnection();
                httpURLConnection.connect();
                Log.d("connect", "Connected");
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "\n");
                    Log.d("downloadadvts", "downloaded video info status result " + sb.toString());
                }
                String res = sb.toString();
                JSONObject jsonObject = new JSONObject(res);
                String parseres = jsonObject.getString("status");
            } catch (MalformedURLException e) {
                Utils.sendLogToServer("Download->Error Exception during updating downloaded video status : Malformed URL Exception" + e, context);
                return false;
            } catch (IOException e) {
                Utils.sendLogToServer("Download->Error Exception during updating downloaded video ststus : IOException " + e, context);
                return false;
            }
            return true;
        } catch (Exception exp) {
            Log.d("downloadadvts", "Failed to update download status in server->" + exp.getMessage());
            LogWriter.writeLogDownload("DownloadAdvts", "Failed to update download status in server->" + exp.getMessage());
            return false;
        }
    }

    boolean checkFileInToDLList(String gdriveID) {
        for (VideoDetailsModel vm : videoDetailsModels) {
            if (vm.getVideo_DriveId().contains(gdriveID)) {
                LogWriter.writeLogDownload("CheckFileInList", "File not to delete as it present in the DL List");
                //Utils.sendLogToServer("Download File canot be deleted File in DN List->"+gdriveID);
                return true;
            }
        }
        return false;
    }

    public boolean deleteForSpace(long size) throws ParseException {
        try {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/advts");
            File[] files = directory.listFiles();
            String path = "";
            if ((files != null) && (files.length > 0)) {
                for (int i = 0; i < files.length; ++i) {

                    String driveID = files[i].getName().substring(0, files[i].getName().indexOf('.'));
                    if (!dataBaseHelper.canDeleteFile(driveID)) continue;
                    if (checkFileInToDLList(driveID)) continue;

                    Utils.sendLogToServer("Download-> Deleting file for space->" + files[i].getName(), context);
                    if (!files[i].delete())
                        Utils.sendLogToServer("Download-> Failed to Delete->" + path, context);
                    LogWriter.writeLogDownload("deleted", "deleted" + path);
                    if (Utils.getAvailableSpace() > size) {
                        Utils.sendLogToServer("Download-> Enough space created", context);
                        return true;
                    }
                }
            } else {
                Utils.sendLogToServer("Download-> No file to delete for space", context);
            }
            return false;
        } catch (Exception exp) {
            Utils.sendLogToServer("Download-> Exception deleting file->" + exp.getMessage(), context);
            return false;
        }

    }

    public boolean deleteAllAdvts() {
        Utils.sendLogToServer("Firebase Command->Deleting all advts ", context);
        File directory = new File(Environment.getExternalStorageDirectory().toString() + "/advts");
        File[] files = directory.listFiles();
        String path = "";
        if ((files != null) && (files.length > 0)) {
            for (int i = 0; i < files.length; ++i) {
                File file = new File(directory, files[i].getName());
                if (file.delete()) {
                    Log.d("downloadadvts", "deleted +" + files[i].getName());
                    Utils.sendLogToServer("Firebase Command->Deleted " + files[i].getName(), context);
                } else {
                    Log.d("downloadadvts", "cant't delete  +" + files[i].getName());
                    Utils.sendLogToServer("Firebase Command->Can't delete " + files[i].getName(), context);

                }


            }
        }
        Log.d("downloadadvts", "calling resetAll from firebase");
        return true;
    }

}


