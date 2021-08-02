package com.example.xhello.domain;

import java.util.ArrayList;

/**
 * 组图网络数据
 */

public class PhotosBean {

    public PhotosData data;

    public class PhotosData {
        public ArrayList<PhotoNews> news;
    }

    public class PhotoNews {
        public String title;
        public String listimage;
    }
}
