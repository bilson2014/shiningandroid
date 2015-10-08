package com.panfeng.shining.Impl;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.panfeng.shining.interfaces.volumnInterface;

public class voiceImpl implements volumnInterface {

	private static AudioManager mAudioManager;
	private static int ring_volumn;
	private static int music_volumn;
	private static int ring_max;
	private static int music_max;
	private static int adjusted_ring;
	private String TAG = "volumn";

	@Override
	public void initVolumn() {
		
			ring_volumn = mAudioManager
					.getStreamVolume(AudioManager.STREAM_RING);
			music_volumn = mAudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			
	
		

	}

	@Override
	public void adjustVolumn() {
		// TODO Auto-generated method stub
		// 铃声转换
		if (ring_max != 0 || music_max != 0) {
			adjusted_ring = (new Double(((double) ring_volumn)
					/ ((double) ring_max) * (music_max))).intValue();
		} else {
			adjusted_ring = ring_volumn;
		}

		mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);

		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, adjusted_ring,
				0);
		

	
		
		
		
		
		

	}

	@Override
	public void resumeVoice() {
		// TODO Auto-generated method stub
		mAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
		mAudioManager.setStreamVolume(AudioManager.STREAM_RING, ring_volumn, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, music_volumn,0);
		
		Log.i(TAG, "resumeVoice ring"+ring_volumn);
		Log.i(TAG, "resumeVoice music"+music_volumn);
		
		
	}

	@Override
	public void initAudio(Context ctx) {
		// TODO Auto-generated method stub
		mAudioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
		ring_max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
		music_max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

	}

}
