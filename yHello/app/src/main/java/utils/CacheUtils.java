package utils;

import android.content.Context;

/**
 * 网络缓存工具类
 * 缓存当前json
 */

public class CacheUtils {
    //写缓存
    //以url为标识，以json为值，保存在本地
    public static  void setCache(Context ctx,String url, String json){
        PrefUtils.putString(ctx,url,json);
    }

    //读缓存
    public  static String getCache(Context ctx,String url){
        return PrefUtils.getString(ctx,url,null);
    }
}
