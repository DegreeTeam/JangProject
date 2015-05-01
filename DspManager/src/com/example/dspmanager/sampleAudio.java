package com.example.dspmanager;

import android.util.Log;

// 이 코드를 패쓰 시켜야 적용이 가능한것 이다.
public class sampleAudio {
	public void write(float[] buffer, int zero, int length){
		for (int i = 1; i <= 8; i++) {
			float sum = 0;
			for (int j = (i - 1) * 512; j < i * 512; j++) {
				sum += buffer[j];
			}
			Log.i("TEST",String.valueOf(i) +" : "+ String.valueOf(sum));
		}
	}
}
