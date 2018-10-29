package com.panfeng.shining.slw.service;

import java.io.File;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
//import android.support.annotation.Nullable;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.slw.utils.FileUtils;

public class VideoBufferService extends Service {

    private HttpUtils httpUtils;
    private static final int maxDownloadThread = 3;
    private int currentposition = 0;
    private int downloadposition = 0;
    private List<VideoEntityLu> list;
    private String netPath = "";
    private String locaPath = "";
    private HttpHandler handler;
    
  
    public static final int UPDATE_EVERYDAY_NEW = 1;
    public static final int UPDATE_RANKING = 2;
    public static final int UPDATE_SORT = 3;

    


    public VideoBufferService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    protected void onHandleIntent(Intent intent) {
        int command = intent.getIntExtra("command", 1);
        currentposition = intent.getIntExtra("position", 0);
        stopDownaload();
        Log.i("dawn",command+"/"+currentposition);
        downloadposition = currentposition;
        switch (command) {
            case VideoBufferService.UPDATE_EVERYDAY_NEW:
                list = DefindConstant.EVERYDAY_NEW;
                break;
            case VideoBufferService.UPDATE_RANKING:
               // list = DefindConstant.RANKING;
                break;
            case VideoBufferService.UPDATE_SORT:
               // list = DefindConstant.SORT;
                break;
        }
        if (!FileUtils.listNotEmpty(list)) {
            return;
        }
        startDownaload();
    }


    private void startDownaload() {
        if(downloadposition>=list.size()||downloadposition>(currentposition+5))
            return;
      //  netPath = DefindConstant.downloadVideoUrl+ list.get(downloadposition).getVideo_id();
     //   locaPath = DefindConstant.saveDownaLoadVideoPath+list.get(downloadposition).getVideo_fIleName();
        Log.i("dawn",netPath);
        Log.i("dawn",locaPath);

        httpUtils = new HttpUtils();
        httpUtils.configRequestThreadPoolSize(maxDownloadThread);
        handler = httpUtils.download(netPath, locaPath, true, true, requestCallBack);
    }

    private void stopDownaload() {
        if (handler != null)
            handler.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("dawn","onDestroy");
        stopDownaload();
    }

  //  @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //////////////////////////////下载回掉////////////////////////////////
    RequestCallBack<File> requestCallBack = new RequestCallBack<File>() {
        /**
         * 完成
         * @param responseInfo
         */
        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            try {
                //db.save(list.get(downloadposition));
            } catch (Exception e) {
                e.printStackTrace();
            }
            startDownaload();
        }

        @Override
        public void onStart() {
            super.onStart();
            downloadposition++;
            Log.i("dawn","onStart"+downloadposition);
        }

        /**
         * 失败
         * @param error
         * @param msg
         */
        @Override
        public void onFailure(HttpException error, String msg) {
            if (msg.equals("maybe the file has downloaded completely")) {
               // downloadposition++;
                Log.i("dawn","onFailure"+downloadposition);
            }else
            {
                Log.i("dawn","onFailure"+downloadposition+msg);
            }
            startDownaload();
        }
    };

}
