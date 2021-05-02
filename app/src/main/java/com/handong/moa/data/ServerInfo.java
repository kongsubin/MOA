package com.handong.moa.data;

public class ServerInfo {
    private static String ip = "";
    private static String port = "";

    public static String getUrl(){
        return "http://" + ip + ":" + port + "/";
    }
}
