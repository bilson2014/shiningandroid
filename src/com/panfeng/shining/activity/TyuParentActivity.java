package com.panfeng.shining.activity;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.os.Bundle;

public class TyuParentActivity extends Activity {
	LocalActivityManager mLocalActivityManager = null;
	private static final String TAG = "TyuParentActivity";
	private static final String STATES_KEY = "android:states";
	static final String PARENT_NON_CONFIG_INSTANCE_KEY = "android:parent_non_config_instance";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocalActivityManager = new LocalActivityManager(this, true);
		Bundle states = savedInstanceState != null ? (Bundle) savedInstanceState
				.getBundle(STATES_KEY) : null;
		mLocalActivityManager.dispatchCreate(states);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLocalActivityManager.dispatchResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Bundle state = mLocalActivityManager.saveInstanceState();
		if (state != null) {
			outState.putBundle(STATES_KEY, state);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocalActivityManager.dispatchPause(isFinishing());
	}

	@Override
	protected void onStop() {
		super.onStop();
		mLocalActivityManager.dispatchStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocalActivityManager.dispatchDestroy(isFinishing());
	}

	/**
	 * Returns a HashMap mapping from child activity ids to the return values
	 * from calls to their onRetainNonConfigurationInstance methods.
	 * 
	 * {@hide}
	 */
	// @Override
	// public HashMap<String, Object> onRetainNonConfigurationChildInstances() {
	// return mLocalActivityManager.dispatchRetainNonConfigurationInstance();
	// }
	// @Override
	// public Object onRetainNonConfigurationInstance() {
	// // TODO Auto-generated method stub
	// return mLocalActivityManager.d;;
	// }
	public Activity getCurrentActivity() {
		return mLocalActivityManager.getCurrentActivity();
	}

	public final LocalActivityManager getLocalActivityManager() {
		return mLocalActivityManager;
	}

}
