package com.tracking.m2comsys.adapplication.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingsModel {
    @SerializedName("command")
    public Command command;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public class Command {
        @SerializedName("commandid")
        private String commandid;

        @SerializedName("deviceid")
        private String deviceid;

        public String getContactDetails() {
            return contactdetails;
        }

        public void setContactDetails(String textmesg) {
            this.contactdetails = textmesg;
        }

        @SerializedName("contactdetails")
        private String contactdetails;


        @SerializedName("action")
        private String action;

        @SerializedName("DevGrpName")
        private String DevGrpName;

        @SerializedName("DevTopic")
        private String DevTopic;
        @SerializedName("emailid")
        private String emailid;
        @SerializedName("password")
        private String password;



        @SerializedName("normal")
        private List<Normal> normal;

        @SerializedName("peak1")
        private List<Peak1> peak1;

        @SerializedName("peak2")
        private List<Peak2> peak2;

        @SerializedName("peak3")
        private List<Peak3> peak3;


        public String getCommandid() {
            return commandid;
        }

        public void setCommandid(String commandid) {
            this.commandid = commandid;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getDevGrpName() {
            return DevGrpName;
        }

        public void setDevGrpName(String devGrpName) {
            DevGrpName = devGrpName;
        }

        public String getDevTopic() {
            return DevTopic;
        }

        public void setDevTopic(String devTopic) {
            DevTopic = devTopic;
        }
        public String getEmailid() {
            return emailid;
        }

        public void setEmailid(String emailid) {
            this.emailid = emailid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<Normal> getNormal() {
            return normal;
        }

        public void setNormal(List<Normal> normal) {
            this.normal = normal;
        }

        public List<Peak1> getPeak1() {
            return peak1;
        }

        public void setPeak1(List<Peak1> peak1) {
            this.peak1 = peak1;
        }

        public List<Peak2> getPeak2() {
            return peak2;
        }

        public void setPeak2(List<Peak2> peak2) {
            this.peak2 = peak2;
        }

        public List<Peak3> getPeak3() {
            return peak3;
        }

        public void setPeak3(List<Peak3> peak3) {
            this.peak3 = peak3;
        }
    }

    public class Normal {
        @SerializedName("DL_Normal")
        private String DL_Normal;

        public String getDL_Normal() {
            return DL_Normal;
        }

        public void setDL_Normal(String DL_Normal) {
            this.DL_Normal = DL_Normal;
        }
    }

    public class Peak1 {
        @SerializedName("DL_Peak1")
        private String DL_Peak1;

        @SerializedName("DL_Idle1")
        private String DL_Idle1;

        @SerializedName("DL_PlayTime1")
        private String DL_PlayTime1;

        public String getDL_Peak1() {
            return DL_Peak1;
        }

        public void setDL_Peak1(String DL_Peak1) {
            this.DL_Peak1 = DL_Peak1;
        }

        public String getDL_Idle1() {
            return DL_Idle1;
        }

        public void setDL_Idle1(String DL_Idle1) {
            this.DL_Idle1 = DL_Idle1;
        }

        public String getDL_PlayTime1() {
            return DL_PlayTime1;
        }

        public void setDL_PlayTime1(String DL_PlayTime1) {
            this.DL_PlayTime1 = DL_PlayTime1;
        }
    }

    public class Peak2 {
        @SerializedName("DL_Peak2")
        private String DL_Peak2;

        @SerializedName("DL_Idle2")
        private String DL_Idle2;

        @SerializedName("DL_PlayTime2")
        private String DL_PlayTime2;

        public String getDL_Peak2() {
            return DL_Peak2;
        }

        public void setDL_Peak2(String DL_Peak2) {
            this.DL_Peak2 = DL_Peak2;
        }

        public String getDL_Idle2() {
            return DL_Idle2;
        }

        public void setDL_Idle2(String DL_Idle2) {
            this.DL_Idle2 = DL_Idle2;
        }

        public String getDL_PlayTime2() {
            return DL_PlayTime2;
        }

        public void setDL_PlayTime2(String DL_PlayTime2) {
            this.DL_PlayTime2 = DL_PlayTime2;
        }
    }

    public class Peak3 {
        @SerializedName("DL_Peak3")
        private String DL_Peak3;

        @SerializedName("DL_Idle3")
        private String DL_Idle3;

        @SerializedName("DL_PlayTime3")
        private String DL_PlayTime3;

        public String getDL_Peak3() {
            return DL_Peak3;
        }

        public void setDL_Peak3(String DL_Peak3) {
            this.DL_Peak3 = DL_Peak3;
        }

        public String getDL_Idle3() {
            return DL_Idle3;
        }

        public void setDL_Idle3(String DL_Idle3) {
            this.DL_Idle3 = DL_Idle3;
        }

        public String getDL_PlayTime3() {
            return DL_PlayTime3;
        }

        public void setDL_PlayTime3(String DL_PlayTime3) {
            this.DL_PlayTime3 = DL_PlayTime3;
        }
    }
}
