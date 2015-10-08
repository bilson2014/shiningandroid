package com.panfeng.shining.activity.newactivity;

import in.srain.cube.mints.base.MintsBaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.panfeng.shining.entity.TypeEntity;
import com.panfeng.shining.fragment.TypeFragment;
import com.panfeng.shinning.R;

public class MoreMeidaActivityV2 extends MintsBaseActivity {
	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moremedia);
        TypeEntity typeEntity=(TypeEntity) getIntent().getExtras().getSerializable("obj");
        Log.e("xyz", typeEntity.getState()+"typeEntity.getState()");
        this.pushFragmentToBackStack(TypeFragment.class,typeEntity);
        initTitleBar();
        
        
    }
    
    void initTitleBar() {
		View title = findViewById(R.id.newlistbar);

		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
	//	bartxt.setText(title_text[keySelectedPosition]);

		bartxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
			}
		});

		barimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

	}
    

    

    protected String getCloseWarning() {
        return "Tap back to exit";
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.id_fragment;
    }
}
