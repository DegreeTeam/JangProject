package com.example.dspmanager;

import android.util.Log;

// �� �ڵ带 �о� ���Ѿ� ������ �����Ѱ� �̴�.
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
