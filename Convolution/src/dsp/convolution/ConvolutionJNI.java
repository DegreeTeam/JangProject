package dsp.convolution;

import android.util.Log;


public class ConvolutionJNI {
	
	static {
		System.loadLibrary("convolution");
	}
	
	private native float[] convolutionfromJNI(float i[]);
	
	public float[] conv(float i[])
	{
		return convolutionfromJNI(i);
	}
	
	public void func(float a){
		Log.i("TEST", String.valueOf(a));
	}
}
