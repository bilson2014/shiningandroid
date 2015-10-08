package com.panfeng.shining.slw.net;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * Created by dawn on 2015/7/7.
 */
public class DownloadFIle {
    private String locaPath;
    private String netPath;

    private int maxDownloadThread=3;
    private HttpUtils http;
    private HttpHandler handler;

    public DownloadFIle(String locaPath, String netPath) {
        this.locaPath = locaPath;
        this.netPath = netPath;
    }


    public void getVideo(RequestCallBack<File> requestCallBack) {

        http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        handler = http.download(netPath,locaPath, true, true, requestCallBack);
    }

    /**
     * 停止下载
     */
    public void stopDownload() {
        if (handler != null) {
            handler.cancel();
        }
    }


    public String getLocaPath() {
        return locaPath;
    }

    public void setLocaPath(String locaPath) {
        this.locaPath = locaPath;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }
}
