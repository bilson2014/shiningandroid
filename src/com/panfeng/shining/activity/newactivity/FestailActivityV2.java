package com.panfeng.shining.activity.newactivity;

import in.srain.cube.mints.base.MintsBaseActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.panfeng.shining.fragment.TypeFragment;
import com.panfeng.shinning.R;

public class FestailActivityV2 extends MintsBaseActivity {

	String name,path;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.festail);
		
		Intent intent = this.getIntent();
	
	
		Bundle bundle =new Bundle();
		bundle.putString("name", intent.getExtras().getString("festailName"));
		bundle.putString("img", intent.getExtras().getString("festailPath"));
		bundle.putString("content", "s");
		bundle.putString("where", "fes");
		
		this.pushFragmentToBackStack(TypeFragment.class,bundle);
		initTitleBar();

	}

	void initTitleBar() {
		View title = findViewById(R.id.newlistbar);

		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		
	
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
