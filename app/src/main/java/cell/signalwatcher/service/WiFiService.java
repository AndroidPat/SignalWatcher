package cell.signalwatcher.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class WiFiService extends Service {

    private final IBinder mBinder = new LocalWiFiBinder();
    WifiManager wifiManager;
    List<ScanResult> results;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalWiFiBinder extends Binder {
        public WiFiService getService() {
            // Return this instance of LocalService so clients can call public methods
            return WiFiService.this;
        }
    }


    @SuppressLint("WifiManagerLeak")
    public List<ScanResult> getWiFiInfo () {
        Log.v("wejfi", "start");

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "wi fi is being enabled", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        results = wifiManager.getScanResults();
        return results;

    }
}
