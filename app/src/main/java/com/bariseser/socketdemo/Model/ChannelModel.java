package com.bariseser.socketdemo.Model;

import java.io.Serializable;

public class ChannelModel implements Serializable {

    private String channelName;
    private String channelDescription;
    private String channelAvatar;
    private String channelNamespace;

    public ChannelModel(String channelName, String channelDescription, String channelAvatar, String channelNamespace) {
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.channelAvatar = channelAvatar;
        this.channelNamespace = channelNamespace;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }

    public String getChannelAvatar() {
        return channelAvatar;
    }

    public void setChannelAvatar(String channelAvatar) {
        this.channelAvatar = channelAvatar;
    }

    public String getChannelNamespace() {
        return channelNamespace;
    }

    public void setChannelNamespace(String channelNamespace) {
        this.channelNamespace = channelNamespace;
    }

    @Override
    public String toString() {
        return "ChannelModel{" +
                "channelName='" + channelName + '\'' +
                ", channelDescription='" + channelDescription + '\'' +
                ", channelAvatar='" + channelAvatar + '\'' +
                ", channelNamespace='" + channelNamespace + '\'' +
                '}';
    }
}
