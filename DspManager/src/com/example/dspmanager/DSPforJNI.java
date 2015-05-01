package com.example.dspmanager;

public class DSPforJNI {

	sampleAudio samAudio = new sampleAudio();

	static {
		System.loadLibrary("dsp-main");
	}

	private native float[] DSPfromJNI(byte i[]);

	public float[] playAfterDSP(byte i[]) {
		return DSPfromJNI(i);
	}

	// Wrapping functions
	public void func(float[] a, int b, int c) {
		samAudio.write(a, b, c);
	}
}
