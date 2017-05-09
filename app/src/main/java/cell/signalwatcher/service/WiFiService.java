package cell.signalwatcher.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cell.signalwatcher.R;
import cell.signalwatcher.Utils.Util;
import cell.signalwatcher.model.DataPoint;

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


    public ArrayList<DataPoint> getWiFiInfo() {

        //checking if wi-fi is enabled and enabling it if required
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "wi fi is being enabled", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }


        results = wifiManager.getScanResults();
        ArrayList<DataPoint> dataPoints = new ArrayList<>();

        //checking for duplicated SSIDs (may be caused by multiple access points or dual band routers
        int size = results.size();
        HashMap<String, Integer> signalStrength = new HashMap<>();
        for (int i = 0; i < size; i++) {
            ScanResult result = results.get(i);
            if (!result.SSID.isEmpty()) {
                String key = result.SSID;
                if (!signalStrength.containsKey(key)) {
                    signalStrength.put(key, i);
                    int scaledLevel = WifiManager.calculateSignalLevel(result.level, 5);
                    String formattedStrength = getString(R.string.format_singal_strength_wifi, String.valueOf(result.level));
                    String formattedMAC = getString(R.string.format_MAC_address, result.BSSID);
                    DataPoint dataPoint = new DataPoint(scaledLevel, result.SSID, Util.getWiFiIcon(scaledLevel), formattedMAC, formattedStrength);
                    dataPoints.add(dataPoint);
                }
            }
        }
        return dataPoints;

    }
}
