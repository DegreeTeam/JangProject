package com.example.dspmanager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DSPActivity extends Activity implements View.OnClickListener {
	TextView tv;
	float[] result = { 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	byte[] data = new byte[4096];
	float[] result1 = new float[8192];

	EditText et1;
	EditText et2;
	EditText et3;
	EditText et4;
	EditText et5;
	EditText et6;
	EditText et7;
	EditText et8;
	Button btn;

	DSPforJNI dspblock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	@Override
	public void onClick(View v) {
		Log.i("TEST", "ONCLICK");
		byte[] poper = new byte[8];
		String p1 = et1.getText().toString();
		String p2 = et2.getText().toString();
		String p3 = et3.getText().toString();
		String p4 = et4.getText().toString();
		String p5 = et5.getText().toString();
		String p6 = et6.getText().toString();
		String p7 = et7.getText().toString();
		String p8 = et8.getText().toString();

		poper[0] = Byte.parseByte(p1);
		poper[1] = Byte.parseByte(p2);
		poper[2] = Byte.parseByte(p3);
		poper[3] = Byte.parseByte(p4);
		
		poper[4] = Byte.parseByte(p5);
		poper[5] = Byte.parseByte(p6);
		poper[6] = Byte.parseByte(p7);
		poper[7] = Byte.parseByte(p8);

		for (int i = 1; i <= 8; i++) {
			for (int j = (i - 1) * 512; j < i * 512; j++) {
				data[j] = (byte)poper[i - 1];
			}
		}
		
		Log.i("BYTE_TEST", String.valueOf(poper[0]));
		Log.i("BYTE_TEST", String.valueOf(poper[1]));
		Log.i("BYTE_TEST", String.valueOf(poper[2]));
		Log.i("BYTE_TEST", String.valueOf(poper[3]));
		
		Log.i("BYTE_TEST", String.valueOf(poper[4]));
		Log.i("BYTE_TEST", String.valueOf(poper[5]));
		Log.i("BYTE_TEST", String.valueOf(poper[6]));
		Log.i("BYTE_TEST", String.valueOf(poper[7]));
		
	

		result1 = dspblock.playAfterDSP(data);

		for (int i = 1; i <= 8; i++) {
			float sum = 0;
			for (int j = (i - 1) * 512; j < i * 512; j++) {
				sum += result1[j];
			}

			result[i - 1] = sum;
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

		dspblock = new DSPforJNI();
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
