package cell.signalwatcher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cell.signalwatcher.CellSignalFragment.BROADCAST_PACKAGE;

/**
 * Background service class
 */

public class ServiceClass extends Service {

    TelephonyManager mTelephonyManager;
    GetSignalInfo mPhoneStatelistener;
    public static int signalStrengthDbm;
    public static String status = "Normal";
    public static int cid = 0;
    public static int lac = 0;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    JSONArray resultsLog = new JSONArray();


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        ServiceClass getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServiceClass.this;
        }
    }


    public void startListenersPhoneState() {
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_SERVICE_STATE | PhoneStateListener.LISTEN_CELL_LOCATION);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mPhoneStatelistener = new GetSignalInfo();
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        startListenersPhoneState();
/*
 * method to test is the file is written to
 */
//        try {
//            readFileTest();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return flags;
    }


    public class GetSignalInfo extends PhoneStateListener {

        @Override
        public void onCellLocationChanged(CellLocation location) {


            if (location != null) {
                if (location instanceof GsmCellLocation) {
                    cid = ((GsmCellLocation) location).getCid();
                    lac = ((GsmCellLocation) location).getLac();
                } else if (location instanceof CdmaCellLocation) {
                    cid = ((CdmaCellLocation) location).getBaseStationId();
                    lac = ((CdmaCellLocation) location).getSystemId();
                }
            }


            Intent i = new Intent(BROADCAST_PACKAGE);
            sendBroadcast(i);

            try {
                try {
                    writeToFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("koko", String.valueOf(cid) + "  " + String.valueOf(lac));

        }


        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99)
                    signalStrengthDbm = signalStrength.getGsmSignalStrength() * 2 - 113;
                else
                    signalStrengthDbm = signalStrength.getGsmSignalStrength();
            } else {
                signalStrengthDbm = signalStrength.getCdmaDbm();
            }

            Intent i = new Intent(BROADCAST_PACKAGE);
            sendBroadcast(i);

            try {
                try {
                    writeToFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("koko", String.valueOf(signalStrengthDbm));
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {

            switch (serviceState.getState()) {
                case ServiceState.STATE_IN_SERVICE:
                    status = "Normal Service";
                    break;
                case ServiceState.STATE_OUT_OF_SERVICE:
                    status = "No Service";
                    break;
                case ServiceState.STATE_EMERGENCY_ONLY:
                    status = "Emergency Only";
                    break;
                case ServiceState.STATE_POWER_OFF:
                    status = "Powered Off";
                    break;
            }

            Intent i = new Intent(BROADCAST_PACKAGE);
            sendBroadcast(i);

            try {
                try {
                    writeToFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v("koko", status);
        }

    }


    public void writeToFile() throws JSONException, FileNotFoundException {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        File file = new File(getFilesDir(), "resultsDat130");
        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = openFileOutput("resultsDat130", MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileInputStream fis = null;
        try {
            fis = openFileInput("resultsDat130");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        String line = null, input = "";
        try {
            while ((line = reader.readLine()) != null)
                input += line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultsLog = new JSONArray();
        if (!input.equals("")) {
            resultsLog = new JSONArray(input);
        }

        JSONObject readings;
        readings = new JSONObject();
        readings.put("cid", cid);
        readings.put("lac", lac);
        readings.put("signalStrengthDbm", signalStrengthDbm);
        readings.put("status", status);
        readings.put("date", date);
        resultsLog.put(readings);

        String text = resultsLog.toString();

        FileOutputStream fos = openFileOutput("resultsDat130", MODE_PRIVATE);


        try {
            fos.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFileTest() throws IOException, JSONException {
        String[] resultingArray;


        FileInputStream fis = openFileInput("resultsDat130");
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        String line = null, input = "";
        while ((line = reader.readLine()) != null)
            input += line;
        reader.close();
        fis.close();

        JSONArray resultsLog = new JSONArray(input);

        resultingArray = new String[resultsLog.length()];
        for (int i = 0; i < resultsLog.length(); i++) {
            resultingArray[i] = resultsLog.getJSONObject(i).getString("status");
            Log.v("readTestFinal", resultingArray[i]);
        }
    }
}
