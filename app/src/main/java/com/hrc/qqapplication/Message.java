package com.hrc.qqapplication;

/**
 * Message的类
 */

public class Message {
    private int message_img;
    private String message_title;
    private String message_context;
    private String message_time;
    private String message_red;

    public String getMessage_red() {
        return message_red;
    }

    public void setMessage_red(String message_red) {
        this.message_red = message_red;
    }

    public int getMessage_img() {
        return message_img;
    }

    public void setMessage_img(int message_img) {
        this.message_img = message_img;
    }

    public String getMessage_title() {
        return message_title;
    }

    public void setMessage_title(String message_title) {
        this.message_title = message_title;
    }

    public String getMessage_context() {
        return message_context;
    }

    public void setMessage_context(String message_context) {
        this.message_context = message_context;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }
}
