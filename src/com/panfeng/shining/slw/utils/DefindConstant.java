package com.panfeng.shining.slw.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.panfeng.shining.entity.PhoneInfoEntity;
import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shining.slw.entity.VideoEntity;

public class DefindConstant {

    /*
    file path
     */
    //错误日志保存文件
    public final static String saveLogPath = FileUtils.getSdcardPaht()
            + File.separator + "a.txt";
    //下载视频保存路径
    public final static String saveDownaLoadVideoPath = FileUtils.getSdcardPaht()
            + File.separator + ".com.panfeng.shining" + File.separator + "DownloadVideo" + File.separator;
    //下载图片保存路径
    public final static String saveDownaLoadImagePath = FileUtils.getSdcardPaht()
            + File.separator + ".com.panfeng.shining" + File.separator + "DownloadImage" + File.separator;
    //数据库保存路径
    public final static String saveDatabasePath = FileUtils.getSdcardPaht()
            + File.separator + ".com.panfeng.shining" + File.separator + "Database" + File.separator;

    public final static DbUtils dbUtils=null;
    public final static BitmapUtils bitmapUtils=null;


    //储存每日鲜列表
    public  static List<VideoEntityLu> EVERYDAY_NEW = new ArrayList<VideoEntityLu>();
    
    
    //排行榜视频列表
    public  static List<VideoEntity> RANKING = new ArrayList<VideoEntity>();
    
    //排行榜视频列表
    public  static List<VideoEntity> FINDING = new ArrayList<VideoEntity>();

    //某一个分类视频列表
    public  static List<VideoEntity> SORT = new ArrayList<VideoEntity>();
    
    public  static List<PhoneInfoEntity> CALLIN = new ArrayList<PhoneInfoEntity>();
    
 //   public  static List<VideoEntity> FESTAIL = new ArrayList<VideoEntity>();


    /*
      net url
     */
}
