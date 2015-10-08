package com.panfeng.shining.data;

import java.util.concurrent.Callable;

import com.panfeng.shining.entity.UserInfoEntity;
import com.panfeng.shining.tools.UserControl;

public class UpdateUser implements Callable<UserInfoEntity> {

	@Override
	public UserInfoEntity call() throws Exception {

		return UserControl.register(null, null);
	}

}
