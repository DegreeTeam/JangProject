package com.example.audiotcp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	Boolean Wifitrue = false;
	ToggleButton playbtn;
	SharedPreferences mPref;
	SharedPreferences.Editor editor;

	Button leftbtn;
	Button rightbtn;
	RoundKnobButton rv;
	Drawable play;
	Drawable stop;
	Drawable left;
	Drawable right;

	Drawable blue_nameimg;
	Drawable red_nameimg;

	TextView nameview;
	View btnView;

	Singleton m_Inst = Singleton.getInstance();

	// Button Click!!
	protected void play() {
		Intent Service = new Intent(MainActivity.this, MainService.class);
		nameview.setBackground(blue_nameimg);
		Service.putExtra("btn", "start");
		startService(Service);
		playbtn.setBackground(stop);
		playbtn.setTextOn("");
		editor.putBoolean("check", true);
	}

	protected void stop() {
		Intent Service = new Intent(MainActivity.this, MainService.class);
		nameview.setBackground(red_nameimg);
		Service.putExtra("btn", "stop");
		startService(Service);
		playbtn.setBackground(play);
		editor.putBoolean("check", false);
		playbtn.setTextOff("");
	}

	protected void next() {
		Log.i("JUST TEST", "NEXT");
	}

	protected void prev() {
		Log.i("JUST TEST", "PREV");
	}

	// INIT !!
	protected void ui_init() {

		mPref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = mPref.edit();
		Boolean checked = mPref.getBoolean("check", false);

		play = getResources().getDrawable(R.drawable.play);
		play.setBounds(0, 0, play.getIntrinsicWidth(),
				play.getIntrinsicHeight());
		stop = getResources().getDrawable(R.drawable.stop);
		stop.setBounds(0, 0, stop.getIntrinsicWidth(),
				stop.getIntrinsicHeight());
		left = getResources().getDrawable(R.drawable.left);
		left.setBounds(0, 0, left.getIntrinsicWidth(),
				left.getIntrinsicHeight());
		right = getResources().getDrawable(R.drawable.right);
		right.setBounds(0, 0, right.getIntrinsicWidth(),
				right.getIntrinsicHeight());

		// TEXT TITLE IMAGE
		blue_nameimg = getResources().getDrawable(R.drawable.blue_textimg);
		red_nameimg = getResources().getDrawable(R.drawable.red_textimg);

		right.setBounds(0, 0, right.getIntrinsicWidth(),
				right.getIntrinsicHeight());

		// 중간 title
		LinearLayout title = new LinearLayout(this);
		title.setId(18);
		LinearLayout.LayoutParams tparam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		tparam.gravity = Gravity.CENTER_HORIZONTAL;
		title.setLayoutParams(tparam);

		nameview = new TextView(this);
		nameview.setBackground(red_nameimg);
		nameview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

		title.addView(nameview);

		// 버튼 상단
		RelativeLayout btn = new RelativeLayout(this);

		playbtn = new ToggleButton(this);
		playbtn.setId(2);
		playbtn.setText("");

		if (checked)
			playbtn.setBackground(stop);
		else
			playbtn.setBackground(play);

		leftbtn = new Button(this);
		leftbtn.setBackground(left);
		leftbtn.setId(1);
		rightbtn = new Button(this);
		rightbtn.setBackground(right);
		rightbtn.setId(3);

		// 3 Button
		RelativeLayout.LayoutParams middleButton = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		middleButton.addRule(RelativeLayout.CENTER_HORIZONTAL);
		playbtn.setLayoutParams(middleButton);

		RelativeLayout.LayoutParams leftButton = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		leftbtn.setLayoutParams(leftButton);
		leftButton.addRule(RelativeLayout.LEFT_OF, 2);

		RelativeLayout.LayoutParams rightButton = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		rightbtn.setLayoutParams(rightButton);
		rightButton.addRule(RelativeLayout.RIGHT_OF, 2);

		btn.addView(playbtn);
		btn.addView(leftbtn);
		btn.addView(rightbtn);

		// circle Button
		m_Inst.InitGUIFrame(this);
		RelativeLayout circle = new RelativeLayout(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		rv = new RoundKnobButton(this, R.drawable.blue_circle,
				R.drawable.red_circle, m_Inst.Scale(1000), m_Inst.Scale(1000));
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		circle.addView(rv, lp);

		// 전체 Linear Layout
		LinearLayout whole = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		whole.setOrientation(LinearLayout.VERTICAL);
		whole.setLayoutParams(params);

		params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		params.topMargin = 500;
		whole.addView(btn, params);
		whole.addView(title);
		whole.addView(circle);

		setContentView(whole);
	}

	protected void listener_init() {

		// CLICK LISTENER
		playbtn.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked == true) {
					play();
					rv.SetState(true);
				} else {
					stop();
					rv.SetState(false);
				}
				editor.commit();
				// TODO Auto-generated method stub
			}
		});

		leftbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				prev();
			}
		});

		rightbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				next();
			}
		});

	}

	// ACTIVITY LIFE CYCLE !!

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ui_init();
		listener_init();
	}

	public void onResume() {
		super.onResume();
	}

	public void onStop() {
		super.onStop();
	}

	protected void onPause() {
		super.onPause();
	}

	public void onDestroy() {
		editor.clear();
		editor.commit();
		stopService(new Intent(MainActivity.this, MainService.class));
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

}