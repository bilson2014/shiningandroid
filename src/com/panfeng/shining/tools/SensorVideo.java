package com.panfeng.shining.tools;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;



import android.os.Environment;





public class SensorVideo {
	
	
	String mediaPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/mySave/";
	
	String[] video = new File(mediaPath).list();;


	public String getVideo(){
		
		List<String> fileList = Arrays.asList(video);

		  Random random = new Random();

	        int s = random.nextInt(fileList.size());
		
		return mediaPath+fileList.get(s);
		
	}
	
	


}
