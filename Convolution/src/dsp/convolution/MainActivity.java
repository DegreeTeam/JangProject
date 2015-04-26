package dsp.convolution;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
	TextView tv;
	float[] result = { 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	float[] data = new float[8192];
	float[] result1 = new float[8192+4];
	
	EditText et1;
	EditText et2;
	EditText et3;
	EditText et4;
	EditText et5;
	EditText et6;
	EditText et7;
	EditText et8;
	Button btn;

	ConvolutionJNI con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	@Override
	public void onClick(View v) {
		Log.i("TEST", "ONCLICK");
		float[] poper = new float[8];
		String p1 = et1.getText().toString();
		String p2 = et2.getText().toString();
		String p3 = et3.getText().toString();
		String p4 = et4.getText().toString();
		String p5 = et5.getText().toString();
		String p6 = et6.getText().toString();
		String p7 = et7.getText().toString();
		String p8 = et8.getText().toString();

		poper[0] = Float.parseFloat(p1);
		poper[1] = Float.parseFloat(p2);
		poper[2] = Float.parseFloat(p3);
		poper[3] = Float.parseFloat(p4);
		poper[4] = Float.parseFloat(p5);
		poper[5] = Float.parseFloat(p6);
		poper[6] = Float.parseFloat(p7);
		poper[7] = Float.parseFloat(p8);

		for (int i = 1; i <= 8; i++) {
			for (int j = (i - 1) * 1024; j < i * 1024; j++) {
				data[j] = poper[i-1];
			}
		}

		result1 = con.conv(data);
		
		for (int i = 1; i <= 8; i++) {
			float sum = 0;
			for (int j = (i - 1) * 1024; j < i * 1024; j++) {
				sum += result1[j];
			}
			
			result[i-1] = sum;
		}

		
		changeResult();

	}

	protected void init() {
		tv = (TextView) findViewById(R.id.result);
		et1 = (EditText) findViewById(R.id.edit1);
		et2 = (EditText) findViewById(R.id.edit2);
		et3 = (EditText) findViewById(R.id.edit3);
		et4 = (EditText) findViewById(R.id.edit4);
		et5 = (EditText) findViewById(R.id.edit5);
		et6 = (EditText) findViewById(R.id.edit6);
		et7 = (EditText) findViewById(R.id.edit7);
		et8 = (EditText) findViewById(R.id.edit8);

		btn = (Button) findViewById(R.id.Button01);
		btn.setOnClickListener(this);

		con = new ConvolutionJNI();
		changeResult();
	}

	protected void changeResult() {
		tv.setText("");
		for (int i = 0; i < 8; i++) {
			tv.setText(tv.getText() + "\n" + String.valueOf(i + 1) + ": "
					+ result[i]);
		}
	}

}
