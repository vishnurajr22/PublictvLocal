package com.tracking.m2comsys.adapplication.utils;

/**
 * Created by m2comsys on 11/7/17.
 */

public class VideoDetailsModel {

    private String Video_Name = "Name";
    private String Video_Extn = "Extension";
    private String Video_DwlDate = "";
    private String Video_DriveId = "DriveId";
    private String Video_LastPlayed ;
    private int Video_NoOfTimesPlayed =0;
    private String Video_Server_Id ;
    private int impressions=0;
    private int vidMapId;

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    private int campaignId;
    private int size=0 ;
    private int errorCount;
    private String endDate;
    private String statusOfDays;
    private int totalImpression;

    public int getRemImpressions() {
        return remImpressions;
    }

    public void setRemImpressions(int remImpressions) {
        this.remImpressions = remImpressions;
    }

    private int remImpressions;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatusOfDays() {
        return statusOfDays;
    }

    public void setStatusOfDays(String statusOfDays) {
        this.statusOfDays = statusOfDays;
    }

    public int getTotalImpression() {
        return totalImpression;
    }

    public void setTotalImpression(int totalImpression) {
        this.totalImpression = totalImpression;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    private int percentage=0 ;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status="ready";

    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getVideo_LastPlayed() {
        return Video_LastPlayed;
    }

    public void setVideo_LastPlayed(String video_LastPlayed) {
        Video_LastPlayed = video_LastPlayed;
    }

    public int getVideo_NoOfTimesPlayed() {
        return Video_NoOfTimesPlayed;
    }

    public void setVideo_NoOfTimesPlayed(int video_NoOfTimesPlayed) {
        Video_NoOfTimesPlayed = video_NoOfTimesPlayed;
    }

    public String getVideo_Server_Id() {
        return Video_Server_Id;
    }

    public void setVideo_Server_Id(String video_Server_Id) {
        Video_Server_Id = video_Server_Id;
    }

    public String getVideo_ServerId() {
        return Video_ServerId;
    }

    public void setVideo_ServerId(String video_ServerId) {
        Video_ServerId = video_ServerId;
    }

    private String Video_ServerId ;




    public String getVideo_Name() {
        return Video_Name;
    }

    public void setVideo_Name(String video_Name) {
        Video_Name = video_Name;
    }

    public String getVideo_Extn() {
        return Video_Extn;
    }

    public void setVideo_Extn(String video_Extn) {
        Video_Extn = video_Extn;
    }

    public String getVideo_DwlDate() {
        return Video_DwlDate;
    }

    public void setVideo_DwlDate(String video_DwlDate) {
        Video_DwlDate = video_DwlDate;
    }

    public String getVideo_DriveId() {
        return Video_DriveId;
    }

    public void setVideo_DriveId(String video_DriveId) {
        Video_DriveId = video_DriveId;
    }

    public int getVidMapId() {
        return vidMapId;
    }

    public void setVidMapId(int vidMapId) {
        this.vidMapId = vidMapId;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }
}
