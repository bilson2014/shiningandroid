package com.panfeng.shining.activity.newactivity;

import in.srain.cube.mints.base.MintsBaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.panfeng.shining.dawn.view.meirixianFragement;
import com.panfeng.shinning.R;

public class EveryDayActivityV2 extends MintsBaseActivity {
	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	GridView gridview;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_everyday);
		initTitleBar();
		this.pushFragmentToBackStack(meirixianFragement.class,
				null);

	}
	


	void initTitleBar() {
		View title = findViewById(R.id.every_bar);
		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
		barimg.setBackgroundResource(R.drawable.everyday);
		bartxt.setText("每日鲜");
		ImageView barbot = (ImageView) title.findViewById(R.id.bar_set);
		barbot.setVisibility(View.VISIBLE);

		barbot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EveryDayActivityV2.this,
						SearchActivity.class);
				startActivity(intent);

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
