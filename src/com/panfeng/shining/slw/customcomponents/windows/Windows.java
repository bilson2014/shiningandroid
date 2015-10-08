package com.panfeng.shining.slw.customcomponents.windows;

/**
 * Created by dawn on 2015/7/14.
 */
//public class Windows {
//    private Context context;
//    private String VideoPath;
//    private WindowManager mWindowManager;
//    private View view;
//
//    private MediaPlayer mediaPlayer;
//    private   SurfaceView surfaceView;
//
//    private boolean oprepared=false;
//    private boolean init=false;
//
//    private Button button;
//
//    public Windows(final Context context, final String videoPath) {
//        this.context = context.getApplicationContext();
//        this.VideoPath = videoPath;
//        mWindowManager = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        init();
//    }
//
//    private void init (){
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        params.type = WindowManager.LayoutParams.TYPE_PHONE;
//        int flags =WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
//
//        params.flags = flags;
//        params.format = PixelFormat.TRANSLUCENT;
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        params.gravity =  Gravity.CENTER;
//        mWindowManager.addView(setUpView(),params);
//        init=true;
//        if(oprepared&&init)
//        {
//            mediaPlayer.start();
//        }
//    }
//    private View setUpView(){
//        view = LayoutInflater.from(context).inflate(R.layout.windows_show_video,
//                null);
//        surfaceView= (SurfaceView)view.findViewById(R.id.show_video_surfaceView1);
//        button=(Button)view.findViewById(R.id.showVideoBtn);
//        Log.i("ssll","button");
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopWindows();
//            }
//        });
//        initMediaPaly(new File(this.VideoPath));
//        return view;
//    }
//
//    public  void stopWindows(){
//        if(init){
//        init=false;
//        stopMediaPlay();
//        mWindowManager.removeView(view);}
//    }
//    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
//        // SurfaceHolder被修改的时候回调
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            // 销毁SurfaceHolder
//            stopMediaPlay();
//        }
//
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//        }
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width,
//                                   int height) {
//        }
//    };
//
//    private void initMediaPaly(File file) {
//        try {
//            if (file != null) {
//                mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                // 设置播放的视频源
//                surfaceView.getHolder().addCallback(callback);
//                mediaPlayer.setDataSource(file.getAbsolutePath());
//                mediaPlayer.prepareAsync();
//                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        //装载完成
//                        // 设置显示视频的SurfaceHolder
//                        oprepared = true;
//                        if (oprepared && init) {
//                            Log.i("dawn", " if (oprepared && init)");
//                            mediaPlayer.setDisplay(surfaceView.getHolder());
//                            mediaPlayer.start();
//                        }
//
//                    }
//                });
//                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//
//                    @Override
//                    public boolean onError(MediaPlayer mp, int what, int extra) {
//                        // 发生错误
//                        mediaPlayer.release();
//                        mediaPlayer = null;
//                        return false;
//                    }
//                });
//            }
//        } catch (Exception e) {
//            // e.printStackTrace();
//            LogUtils.writeErrorLog("播放视频文件", "", e, null);
//        }
//    }
//
//    private void stopMediaPlay() {
//        try {
//            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                mediaPlayer.stop();
//                mediaPlayer.release();
//                mediaPlayer = null;
//            }
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        }
//
////        try {
////            VideoEntity videoEntity =db.findFirst(Selector.from(VideoEntity.class).where("Video_id", "=", ve.getVideo_id()));
////        } catch (DbException e) {
////            e.printStackTrace();
////        }
//    }
//    //////////////////////////////////视频播放相关/////////////////////////////////////////////////////
//}
