package com.panfeng.shining.activity.newactivity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.ImageView;

import android.widget.TextView;


import com.panfeng.shinning.R;


public class WebViewActivity extends Activity {


	WebView webView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
	
		initTitleBar();
	
		init();
		
		
	}
	
	 private void init(){
		 
	
	
		 
	        webView = (WebView) findViewById(R.id.webView);
	        //WebView加载web资源
	      
	  	
	  	//支持javascript
	       webView.getSettings().setJavaScriptEnabled(true); 
	  	// 设置可以支持缩放 
	       webView.getSettings().setSupportZoom(true); 
	  	// 设置出现缩放工具 
	       webView.getSettings().setBuiltInZoomControls(true);
	  	//扩大比例的缩放
	       webView.getSettings().setUseWideViewPort(true);
	  	//自适应屏幕
	       webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	       webView.getSettings().setLoadWithOverviewMode(true);
	       webView.setInitialScale(150);
	       webView.loadUrl("http://123.57.32.50/1.html");
	  	 

	        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
	       webView.setWebViewClient(new WebViewClient(){
	           @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            // TODO Auto-generated method stub
	               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
	             view.loadUrl(url);
	            return true;
	        }
	       });
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
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
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
}
