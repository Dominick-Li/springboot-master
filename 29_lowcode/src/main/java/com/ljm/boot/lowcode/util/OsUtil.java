package com.ljm.boot.lowcode.util;

public class OsUtil {

    private OsUtil(){ }

    public  static String getSuffix(){
        String os = System.getProperty("os.name").toLowerCase();
        String suffix = ".dll";
        //linux环境
        if (os != null && os.indexOf("linux") != -1) {
            suffix = ".so";
        }
        return suffix;
    }

    public  static boolean isLinuxOs(){
        String os = System.getProperty("os.name").toLowerCase();
        return os != null && os.indexOf("linux") != -1;
    }

    public  static  boolean isWindowOs(){
        String os = System.getProperty("os.name").toLowerCase();
        return  os != null && os.indexOf("window") != -1;
    }

    public  static  String getOsName(){
        return System.getProperty("os.name").toLowerCase();
    }
}
