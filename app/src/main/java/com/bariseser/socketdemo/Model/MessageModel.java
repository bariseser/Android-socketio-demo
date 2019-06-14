package com.bariseser.socketdemo.Model;

public class MessageModel {

    public static final int MY_MESSAGE = 0;
    public static final int THEIR_MESSAGE = 1;
    public static final int LOG = 2;

    private String nickname;
    private String message;
    private int type;

    public MessageModel() {
    }

    public MessageModel(String nickname, String message, int type) {
        this.nickname = nickname;
        this.message = message;
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String username) {
        this.nickname = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
