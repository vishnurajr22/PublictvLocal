package com.tracking.m2comsys.adapplication.utils;

/**
 * Created by m2 on 31/7/17.
 */

public class AdvtHistory {


    private String Video_DwlDate ;
    private String Video_LastPlayed = "";

    public int getVideo_NoOfTimesPlayed() {
        return Video_NoOfTimesPlayed;
    }

    public void setVideo_NoOfTimesPlayed(int video_NoOfTimesPlayed) {
        Video_NoOfTimesPlayed = video_NoOfTimesPlayed;
    }

    private  int Video_NoOfTimesPlayed =0;

    public String getVideo_DwlDate() {
        return Video_DwlDate;
    }

    public void setVideo_DwlDate(String video_DwlDate) {
        Video_DwlDate = video_DwlDate;
    }

    public String getVideo_LastPlayed() {
        return Video_LastPlayed;
    }

    public void setVideo_LastPlayed(String video_LastPlayed) {
        Video_LastPlayed = video_LastPlayed;
    }


    public int getVideo_Server_Id() {
        return Video_Server_Id;
    }

    public void setVideo_Server_Id(int video_Server_Id) {
        Video_Server_Id = video_Server_Id;
    }

    private int Video_Server_Id ;



}
