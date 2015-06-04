package com.example.audiotcp;

import java.util.List;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	// DEBUG
	public static final String TAG = "BLETEST";

	// STATIC VARIABLE
	public static RecHandler RHandler = null;
	public static boolean Activity_ready = false;
	public static Context mcontext;
	public static WifiManager manager; /* */

	// STATIC FLAG
	public static final int BLE_CATCH_SIGNAL = 0;
	Boolean muteflag = false;
	Boolean first = true;
	// UI
	SharedPreferences mPref;
	SharedPreferences.Editor editor;

	ToggleButton playbtn;
	Button rightbtn;
	Button leftbtn;
	TextView title;
	SeekBar sound;
	Button refreshbtn;
	// Queue - BLE
	public BLEArr curBLE;

	// Audio
	AudioManager mgr = null;

	// wifi check
	NetworkReceiver receiver;
	public static boolean wificonnection = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RHandler = new RecHandler();
		mcontext = this;
		mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		receiver = new NetworkReceiver();
		IntentFilter wifiFilter = new IntentFilter();
		wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); //
		this.registerReceiver(receiver, wifiFilter);
		ui_init();
	}

	@Override
	protected void onResume() {
		Activity_ready = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		editor.clear();
		editor.commit();
		stopService(new Intent(MainActivity.this, MainService.class));
		stopService(new Intent(MainActivity.this, BLEService.class));
		Activity_ready = false;
		mgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
		super.onDestroy();
	}

	protected void update() {

		curBLE = BLEService.cqueue.connectble;
		String text = curBLE.getUUid();
		if (text != null)
			title.setText(text.substring(0, 8));

	}

	// Button Click!!
	protected void play() {
		if (BLEService.IsBLESignal) {
			Intent Service = new Intent(MainActivity.this, MainService.class);
			first = true;
			muteflag = false;
			Service.putExtra("btn", "start");
			startService(Service);
			playbtn.setButtonDrawable(R.drawable.center_stop);
			playbtn.setTextOn("");
			editor.putBoolean("check", true);
			update();
		} else
			Toast.makeText(mcontext, "NOT BLE SIGNAL", Toast.LENGTH_SHORT)
					.show();
	}
	
	protected void pause() {
		if (muteflag == false){
			mgr.setStreamMute(AudioManager.STREAM_MUSIC, true);
			playbtn.setButtonDrawable(R.drawable.center);
			muteflag = true;
		} else {
			mgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
			playbtn.setButtonDrawable(R.drawable.center_stop);
			muteflag = false;
		}
			
	}
	
	protected void stop() {
		if (BLEService.IsBLESignal) {
			Intent Service = new Intent(MainActivity.this, MainService.class);
			Service.putExtra("btn", "stop");
			startService(Service);
			playbtn.setButtonDrawable(R.drawable.center);
			editor.putBoolean("check", false);
			playbtn.setTextOff("");
			muteflag = false;
			first = true;
			mgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
		} else
			Toast.makeText(mcontext, "NOT BLE SIGNAL", Toast.LENGTH_SHORT)
					.show();

	}

	protected void next() {
		if (BLEService.IsBLESignal) {
			if (BLEService.cqueue.rear != BLEService.cqueue.front)
				wificonnection = false;
			stop();
			new AsyncWifiConnect().execute(BLEService.cqueue.next());
			update();
		} else
			Toast.makeText(mcontext, "NOT BLE SIGNAL", Toast.LENGTH_SHORT)
					.show();
	}

	protected void prev() {
		if (BLEService.IsBLESignal) {
			if (BLEService.cqueue.rear != BLEService.cqueue.front)
				wificonnection = false;
			stop();
			new AsyncWifiConnect().execute(BLEService.cqueue.next());
			update();
		} else
			Toast.makeText(mcontext, "NOT BLE SIGNAL", Toast.LENGTH_SHORT)
					.show();
	}

	// INIT !!
	protected void ui_init() {

		mPref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = mPref.edit();
		Boolean checked = mPref.getBoolean("check", false);

		setContentView(R.layout.activity_main);

		playbtn = (ToggleButton) this.findViewById(R.id.playbtn);
		rightbtn = (Button) this.findViewById(R.id.right);
		leftbtn = (Button) this.findViewById(R.id.left);
		title = (TextView) this.findViewById(R.id.title);
		sound = (SeekBar) this.findViewById(R.id.seekBar);
		refreshbtn = (Button)this.findViewById(R.id.refresh);
		
		initBar(sound, AudioManager.STREAM_MUSIC);// for Volume this is
													// neccessary
		refreshbtn.setOnClickListener(new OnClickListener()
		   {
		    public void onClick(View view)
		    {
		    	if(isServiceRunningCheck()) {
		    		Log.i("TESTservice", "stopservice");
		    		stopService(new Intent(MainActivity.this, MainService.class));
		    	}
		    	new AsyncWifiConnect().execute(BLEService.cqueue.next());
	    		Log.i("service", "connect");
				playbtn.setButtonDrawable(R.drawable.center);
				title.setText("재생 버튼을 눌러주세요");
				mgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
		    	first = true;
		    	
		     }
		    }
		);
		// CLICK LISTENER
		playbtn.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				/*
				if (isChecked == true)
					play();
				else
					stop(); */
				if (first) {
					play();
					first = false;
				} else {
						pause();
				}
				editor.commit();
				// TODO Auto-generated method stub
			}
		});

		leftbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				prev();
				if(isServiceRunningCheck()) {
		    		Log.i("TESTservice", "servicestop");
		    		stopService(new Intent(MainActivity.this, MainService.class));
		    	}

				Log.i("JUSTTEST", "left");
			}
		});

		rightbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				next();
				if(isServiceRunningCheck()) {
		    		Log.i("TESTservice", "servicestop");
		    		stopService(new Intent(MainActivity.this, MainService.class));
		    	}

				Log.i("JUSTTEST", "right");
			}
		});
	}
	
	public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.audiotcp.MainService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
}
	
	private void initBar(SeekBar bar, final int stream) {
		bar.setMax(mgr.getStreamMaxVolume(stream));
		bar.setProgress(mgr.getStreamVolume(stream));

		bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar bar, int progress,
					boolean fromUser) {
				// mgr.setStreamVolume(stream, progress,
				// AudioManager.FLAG_PLAY_SOUND);
				mgr.setStreamVolume(stream, progress, 0);
			}

			public void onStartTrackingTouch(SeekBar bar) {

				// no-op
			}

			public void onStopTrackingTouch(SeekBar bar) {
				// no-op
			}
		});
	}

	public static void wificonnect(String uuid) {
		String ssid = uuid.substring(0, 8);
		String password = uuid.substring(8, 16);

		Log.i("wificonnect", ssid);
		Log.i("wificonnect", password);
		manager = (WifiManager) mcontext.getSystemService(Context.WIFI_SERVICE);
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
				Thread.sleep(3000);
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
		}
		manager.enableNetwork(networkId, true);
	}

	private static class AsyncWifiConnect extends AsyncTask<String, Void, Void> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(mcontext);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage("WIFI CONNECTING");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(String... str) {
			int i;
			wificonnect(str[0]);
			try {
				Thread.sleep(5000);
			
				for (i = 0; i < 1000; i++) {
					if (wificonnection) 
						break;
					Thread.sleep(500);
				}
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return (Void) null;
		}

		@Override
		protected void onProgressUpdate(Void... v) {
			// TODO show progress
		}

		@Override
		protected void onPostExecute(Void v) {
			progressDialog.dismiss();
		}
	}

	// Handler
	public static class RecHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case BLE_CATCH_SIGNAL:
				new AsyncWifiConnect().execute(BLEService.cqueue.next());
			default:
				break;
			}
		}
	};

	public static class NetworkReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (intent.getAction().equals(
					WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				NetworkInfo networkInfo = intent
						.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

				if (networkInfo.isConnected()) {
					Log.d("networkok", "connect..");
					wificonnection = true;
				}
			}
		}

	}

}