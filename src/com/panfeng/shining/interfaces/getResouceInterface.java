package com.panfeng.shining.interfaces;

import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestHandler;

public interface getResouceInterface {

	// page=页数|keySelectedPosition=关键字索引，keywordArray=关键字（List），name=关键字（String），startID=起始id

	void getValues(RequestHandler<JsonData> requestHandler, int currPageID,
			int size, int startID, String type);

}
