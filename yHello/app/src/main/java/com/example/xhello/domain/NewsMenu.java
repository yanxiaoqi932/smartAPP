package com.example.xhello.domain;

import java.util.ArrayList;

/**
 * 分类数据的封装，根据具体json的情况来定义变量
 *
 * 使用Gson解析，对象的书写方式：
 * 1.逢{ }创建对象
 * 2.逢[]创建集合
 * 3.对象中所有字段名称必须和json完全一致
 * 4.创建类时，类名可以随意定义
 */

public class NewsMenu {
    public int retcode;
    public ArrayList<NewsMenuData> data;
    public ArrayList<String> extend;


    //四个分类菜单的信息：新闻，专题，组图，互动
    public class NewsMenuData{
        public String id;
        public String title;
        public int type;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    //12个页签的对象
    public class NewsTabData{
        public String id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsMenu{" +
                "data=" + data +
                '}';
    }
}

