package com.example.rabbi.values;

public class Fields {
    public static String getServerIp(){
        return "192.168.0.101";
    }
    public static String getServerPort(){
        return "5000";
    }
    public static String getServerRoute(){
        return "receiver";
    }
    public static String getServerUrl(){
        return "http://"+getServerIp()+":"+getServerPort()+"/"+getServerRoute();
    }
}
