package com.example.audiotcp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
/*
public class NetworkChangeReceiver extends BroadcastReceiver {
	 
   @Override
    public void onReceive(final Context context, final Intent intent) {
 
    	   final String action = intent.getAction();
    	   boolean connected = true;
    	   
    	    if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
    	        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
    	        connected = info.isConnected();
    	        Log.d("queue","here");
    	    }
    	        if(!connected) {
    	        	Intent broadi = new Intent(MainActivity.context, MainActivity.class);
    	        	intent.putExtra("broadcast", "stop");
    	        	MainActivity.context.startActivity(broadi);
    	        }
    	   
    }
}
*/
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    Intent msg = new Intent(context, MainActivity.class);
        boolean isConnected = wifi != null && wifi.isConnectedOrConnecting();
        if (isConnected) {
     //       Log.d("Network", "YES");
            msg.putExtra("wifistaus", "yes");
        }else{
       //    Log.d("Network", "NO");
           msg.putExtra("wifistaus", "no");
        }
     //   context.startActivity(msg);
    }
}