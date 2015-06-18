package com.catchwave.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.catchwave.service.PlayService;

public class PlayerActivity extends Activity {

	public static Activity playerActivity;

	Button playBtn;
	Button stopBtn;
	TextView tv;
	String uuidData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		playerActivity = this;
		setContentView(R.layout.activity_player);

		playBtn = (Button) findViewById(R.id.playBtn);
		playBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				startService(new Intent(getApplicationContext(),
						PlayService.class));
			}
		});

		stopBtn = (Button) findViewById(R.id.stopBtn);
		stopBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				stopService(new Intent(getApplicationContext(),
						PlayService.class));
			}
		});

		// Get UUID
		Intent intent = getIntent();
		uuidData = intent.getExtras().getString("UUID");

		tv = (TextView) findViewById(R.id.tv);
		String name = uuidData.substring(0, 8);
		tv.setText(name);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(new Intent(getApplicationContext(), PlayService.class));
	}
}
