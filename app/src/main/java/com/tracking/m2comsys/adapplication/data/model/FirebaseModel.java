package com.tracking.m2comsys.adapplication.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FirebaseModel {
    @SerializedName("body")
    private Body body;
    @SerializedName("title")
    private String title;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public class Body {
        @SerializedName("timestamp")
        private String timestamp;
        @SerializedName("deviceuuid")
        private List<Deviceuuid> deviceUuid;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public List<Deviceuuid> getDeviceUuid() {
            return deviceUuid;
        }

        public void setDeviceUuid(List<Deviceuuid> deviceUuid) {
            this.deviceUuid = deviceUuid;
        }
    }

    public class Deviceuuid {
        @SerializedName("DL_DeviceUUID")
        public String DL_DeviceUUID;

        public String getDL_DeviceUUID() {
            return DL_DeviceUUID;
        }

        public void setDL_DeviceUUID(String DL_DeviceUUID) {
            this.DL_DeviceUUID = DL_DeviceUUID;
        }
    }
}
