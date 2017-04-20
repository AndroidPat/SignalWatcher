package cell.signalwatcher;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static cell.signalwatcher.ServiceClass.cid;
import static cell.signalwatcher.ServiceClass.lac;
import static cell.signalwatcher.ServiceClass.signalStrengthDbm;
import static cell.signalwatcher.ServiceClass.status;


/**
 * A simple {@link Fragment} subclass.
 */
public class CellSignalFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int REQUEST_READ_PHONE_STATE = 0;
    public static final int REQUEST_COARSE_LOCATION = 1;
    ServiceClass mService;
    boolean mBound = false;

    private Unbinder unbinder;

    @BindView(R.id.tvCid)
    TextView tvCid;
    @BindView(R.id.tvLac)
    TextView tvLac;
    @BindView(R.id.tvServiceSt)
    TextView tvServiceSt;
    @BindView(R.id.tvSignalStr)
    TextView tvSignalStr;


    public CellSignalFragment() {
        // Required empty public constructor
    }

    public static CellSignalFragment newInstance() {
        CellSignalFragment fragment = new CellSignalFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cell_signal, container, false);

        ButterKnife.bind(this, view);

        // Bind to LocalService
        Intent intent = new Intent(getActivity(), ServiceClass.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


        checkPermissions();

        // Inflate the layout for this fragment
        return view;

    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServiceClass.LocalBinder binder = (ServiceClass.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            tvSignalStr.setText(String.valueOf(signalStrengthDbm));
            tvServiceSt.setText(status);
            tvCid.setText(String.valueOf(cid));
            tvLac.setText(String.valueOf(lac));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void checkPermissions() {

        int permissionCheckPhoneState = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE);
        int permissionCheckLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);


        if (permissionCheckPhoneState != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else if (permissionCheckLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
        } else {
            Intent intent = new Intent(getActivity(), ServiceClass.class);
            getActivity().startService(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Intent intent = new Intent(getActivity(), ServiceClass.class);
                    getActivity().startService(intent);
                } else {
                    Log.v("CallFragment", "no permission phone_state");
                }
            case REQUEST_COARSE_LOCATION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Intent intent = new Intent(getActivity(), ServiceClass.class);
                    getActivity().startService(intent);
                } else {
                    Log.v("CallFragment", "no permission phone_location");
                    break;
                }

            default:
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }

    }


}