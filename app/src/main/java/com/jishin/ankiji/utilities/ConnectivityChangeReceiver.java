package com.jishin.ankiji.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by trungnguyeen on 1/27/18.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver{

    private static final String TAG = ConnectivityChangeReceiver.class.getSimpleName();
    private NetworkListener callback;

    public ConnectivityChangeReceiver(NetworkListener callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (checkConnectionFromSystem(context)){
            Log.i(TAG, "Internet connected");
            callback.connected();
        }
        else{
            Log.i(TAG, "No internet");
            callback.notConnected();
        }
    }


    public boolean checkConnectionFromSystem(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
