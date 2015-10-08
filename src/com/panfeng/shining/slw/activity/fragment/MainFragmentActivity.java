package com.panfeng.shining.slw.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.panfeng.shinning.R;



import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainFragmentActivity extends FragmentActivity {

    @ViewInject(R.id.id_viewpager)
    private ViewPager myViewPager;

    @ViewInject(R.id.textView1)
    private TextView bar_tv1;

    @ViewInject(R.id.textView2)
    private TextView bar_tv2;

    @ViewInject(R.id.textView3)
    private TextView bar_tv3;

    @ViewInject(R.id.textView4)
    private TextView bar_tv4;

    @ViewInject(R.id.textView5)
    private TextView bar_tv5;

    private String tag = "dawn";
    private FragmentPagerAdapter myAdapter;
    private List<Fragment> myFragmentsList;

    private EveryDayFragment videoListFragment;
    private VideoHotListFragment videoHotListFragment;
    private VideoSortListFragment videoSortListFragment;
    private SetingFragment setingFragment;
    private ShootFragment shootFragment;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewUtils.inject(this);
        initFragment();
        myViewPager.setAdapter(myAdapter);
        myViewPager.setOnPageChangeListener(opcl);
        resetBar();
        selectItem(0);
    }

    void initFragment() {
        myFragmentsList = new ArrayList<Fragment>();

        videoListFragment = new EveryDayFragment();
        videoHotListFragment = new VideoHotListFragment();
        videoSortListFragment = new VideoSortListFragment();
        setingFragment = new SetingFragment();
        shootFragment = new ShootFragment();

        myFragmentsList.add(videoListFragment);
        myFragmentsList.add(videoHotListFragment);
        myFragmentsList.add(videoSortListFragment);
        myFragmentsList.add(shootFragment);
        myFragmentsList.add(setingFragment);

        myAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return myFragmentsList.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return myFragmentsList.get(arg0);
            }
        };
        bar_tv1.setOnClickListener(ockl);
        bar_tv2.setOnClickListener(ockl);
        bar_tv3.setOnClickListener(ockl);
        bar_tv4.setOnClickListener(ockl);
        bar_tv5.setOnClickListener(ockl);

    }

    OnPageChangeListener opcl = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            int currentItem = myViewPager.getCurrentItem();
            Log.i(tag, currentItem + "");
            selectItem(currentItem);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    OnClickListener ockl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textView1:
                    selectItem(0);
                    break;

                case R.id.textView2:
                    selectItem(1);
                    break;

                case R.id.textView3:
                    selectItem(2);
                    break;
                case R.id.textView4:
                    selectItem(3);
                    break;
                case R.id.textView5:
                    selectItem(4);
                    break;
            }
        }
    };

    private void resetBar() {
        bar_tv1.setTextColor(Color.WHITE);
        bar_tv2.setTextColor(Color.WHITE);
        bar_tv3.setTextColor(Color.WHITE);
        bar_tv4.setTextColor(Color.WHITE);
        bar_tv5.setTextColor(Color.WHITE);
    }

    private void selectItem(int i) {
        myViewPager.setCurrentItem(i);
        setBarText(i);
    }

    private void setBarText(int i) {
        resetBar();
        switch (i) {
            case 0:
                Log.i(tag, i + "");
                bar_tv1.setTextColor(Color.BLACK);
                break;
            case 1:
                Log.i(tag, i + "");
                bar_tv2.setTextColor(Color.BLACK);
                break;
            case 2:
                Log.i(tag, i + "");
                bar_tv3.setTextColor(Color.BLACK);
                break;
            case 3:
                Log.i(tag, i + "");
                bar_tv4.setTextColor(Color.BLACK);
                break;
            case 4:
                Log.i(tag, i + "");
                bar_tv5.setTextColor(Color.BLACK);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
