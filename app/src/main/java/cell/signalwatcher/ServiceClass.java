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
            Log.v("koko", status);
        }

    }
}
