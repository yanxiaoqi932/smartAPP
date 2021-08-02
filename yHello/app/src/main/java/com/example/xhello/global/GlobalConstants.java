package com.example.xhello.global;

/**
 * Tomcat框架连接服务器 端口:8080
 */

public class GlobalConstants {
    //服务器根地址
    //原生模拟器使用10.0.2.2:8080
    public static final String SERVERL_URL = "http://172.17.67.241:8080/zhbj";
    //分类接口地址
    public static final String CATEGORY_URL = SERVERL_URL + "/categories.json";
    //组图接口地址
    public static final String PHOTOS_URL = SERVERL_URL + "/photos/photos_1.json";
}

