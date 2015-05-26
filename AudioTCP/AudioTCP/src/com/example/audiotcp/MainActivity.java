package com.example.audiotcp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	public static Context context;
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

	// BLE
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private Handler mHandler;
	private CircularQueue cqueue;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;

    int front = 0; 
    int rear = 0;
    
	//Wifi 
	WifiManager manager;
	NetworkChangeReceiver receiver;
	
//	boolean flag = false;
	BLEArr bldevice;
	int size = 5;
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
		BLEArr connectble = new BLEArr(); 
		if (front < rear)
			front++;
		else
			front = 0;
		connectble = ((BLEArr) cqueue.GetElement(front));
		String uuid = connectble.getUUid();
		connectwifi(uuid);
	}

	protected void prev() {
		Log.i("JUST TEST", "PREV");
		BLEArr connectble = new BLEArr(); 
		if (front == 0)
			front = rear;
		else
			front--;

		connectble = ((BLEArr) cqueue.GetElement(front));
		String uuid = connectble.getUUid();
		connectwifi(uuid);
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
        nameview.setGravity(Gravity.CENTER);
        
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
					rv.ChangeView(true);
				} else {
					stop();
					rv.ChangeView(false);
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
		mHandler = new Handler();
		bluetoothcheck();
		ui_init();
		listener_init();
	}

	public void onResume() {
		super.onResume();
		cqueue = new CircularQueue(size);
		scanLeDevice(true);
	}

	public void onStop() {
		super.onStop();
	}
	
	protected void onNewIntent (Intent intent) {
		super.onNewIntent(intent);
		/*	setIntent(intent);
		String str = intent.getStringExtra("wifistaus");
		if(str != null) {
		if(str.equals("no"))
			stop();
		else if (str.equals("yes"))
			play();
		Log.d("Network", "YES");
		}*/
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

	public void bluetoothcheck() {
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "ble_not_supported", Toast.LENGTH_SHORT)
					.show();
			finish();
		}

		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		mBluetoothAdapter.enable();

		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "ble_not_supported", Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}
	}

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);				
				}
			}, SCAN_PERIOD);
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);

		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {

			final String uuid = getUid(device, scanRecord);
			final int Rssi = rssi;
		
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					if (device != null) {
						bldevice = new BLEArr();
						bldevice.setdevice(device, Rssi, uuid);
						if(!cqueue.contains(device)) {	
							cqueue.insert(bldevice);
							Log.d("queue","insert");
						}
						front = cqueue.front;
						rear = cqueue.rear;
				     	connectwifi(((BLEArr) cqueue.GetElement(cqueue.front)).getUUid());
				     	nameview.setText(((BLEArr) cqueue.GetElement(cqueue.front)).getDevice().getName());
					}
				}
			});
			
		}
	};

	String getUid(BluetoothDevice device, byte[] scanRecord) {
		int startByte = 2;

		boolean patternFound = false;
		String uuid = new String();
		while (startByte <= 5) {
			if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && // Identifies
																	// an
																	// iBeacon
					((int) scanRecord[startByte + 3] & 0xff) == 0x15) { // Identifies
																		// correct
																		// data
																		// length
				patternFound = true;
				break;
			}
			startByte++;
		}

		if (patternFound) {
			// Convert to hex String
			byte[] uuidBytes = new byte[17];
			System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 17);

			for (int i = 0; i < 17; i++) {
				uuid += (char) uuidBytes[i];
			}
		}
		return uuid;
	}

	void connectwifi(String uuid) {

		// 해당 AP로 연결하기
		String ssid = uuid.substring(0, 8);
		String password = uuid.substring(8, 16);
		Log.i("bleuuid", ssid);
		Log.i("bleuuid", password);
		manager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo winfo = manager.getConnectionInfo();

		String getSSID = winfo.getSSID(); // WifiInfo 에서 받아온 SSID정보
		boolean isConfigured = false;
		int networkId = -1;
		boolean wifiEnable = manager.isWifiEnabled(); // WIFI status

		if (wifiEnable == false) // WIFI on
		{
			manager.setWifiEnabled(true);
			// wifi 0n and 2.0 second wait
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (ssid != getSSID) {
			List<WifiConfiguration> configList = manager
					.getConfiguredNetworks(); // WIFI 정보
			// 설정된 wifiConfig 불러와서 있으면 network id를 가져옴
			for (WifiConfiguration wifiConfig : configList) {
				String str = wifiConfig.SSID;
				if (wifiConfig.SSID.equals("\"".concat(ssid).concat("\""))) {
					Log.d("wifi", "SSID : " + str);
					networkId = wifiConfig.networkId;
					isConfigured = true;
					break;
				}
			}

		}

		if (!isConfigured) {
			WifiConfiguration wfc = new WifiConfiguration();

			wfc.SSID = "\"".concat(ssid).concat("\"");
			wfc.status = WifiConfiguration.Status.DISABLED;
			wfc.priority = 40;

			// WPA/WPA2
			wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			wfc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			wfc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wfc.preSharedKey = "\"".concat(password).concat("\"");

			// wfc 값으로 설정하여 추가합니다.
			networkId = manager.addNetwork(wfc);
			// 설정한 값을 저장합니다.
			manager.saveConfiguration();
			Log.d("Network","networkon");
	//		registerReceiver(receiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
		}
	   
	}
	
}