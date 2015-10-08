package com.panfeng.shining.slw.activity.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shinning.R;


public class VideoHotListFragment extends Fragment {
    @ViewInject(R.id.button)
    private Button button;
    MyHandler m=new MyHandler();

  
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=getActivity();
        View view = inflater.inflate(R.layout.fragment_video_hot, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      

    }
    @OnClick(R.id.button)
    public void tan(View view){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                m.sendEmptyMessage(1);
            }
        }).start();

    }
    class  MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                final String video=DefindConstant.saveDownaLoadVideoPath+"123.mp4";
           //     Windows w=new Windows(context, video);
            }
        }
    }
}
