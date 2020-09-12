package com.ljm.boot.nettywebsocket.model;

/**
 * @author Dominick Li
 * @createTime 2020/3/4 19:58
 * @description socket接消息实体
 **/
public class OnlineMessage {

    /**
     * 消息发送者id
     */
    private String sendId;
    /**
     * 消息接受者id
     */
    private String acceptId;
    /**
     * 消息内容
     */
    private String message;

    /**
     * 头像
     */
    private String headImg;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getAcceptId() {
        return acceptId;
    }

    public void setAcceptId(String acceptId) {
        this.acceptId = acceptId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
