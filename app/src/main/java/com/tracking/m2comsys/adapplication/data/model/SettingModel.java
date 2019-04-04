package com.tracking.m2comsys.adapplication.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tikson on 8/5/18.
 */

public class SettingModel {
    @SerializedName("command")
    public Command command;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public static class Command {
        @SerializedName("commandid")
        public String commandid;
        @SerializedName("settingsver")
        public String settingsver;
        @SerializedName("action")
        public String action;
        @SerializedName("DevGrpName")
        public String DevGrpName;
        @SerializedName("DevTopic")
        public String DevTopic;

        public String getCommandid() {
            return commandid;
        }

        public void setCommandid(String commandid) {
            this.commandid = commandid;
        }

        public String getSettingsver() {
            return settingsver;
        }

        public void setSettingsver(String settingsver) {
            this.settingsver = settingsver;
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
    }
}
