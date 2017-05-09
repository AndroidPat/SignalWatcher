package cell.signalwatcher.ui;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cell.signalwatcher.R;
import cell.signalwatcher.Utils.Constants;
import cell.signalwatcher.Utils.Util;
import cell.signalwatcher.adapters.WiFiAdapter;
import cell.signalwatcher.model.DataPoint;
import cell.signalwatcher.service.WiFiService;


/**
 * A simple {@link Fragment} subclass.
 */
public class WiFiSignalFragment extends Fragment {


    WiFiService mWifiService;
    boolean mBound = false;
    private Unbinder unbinder;
    private ArrayList<DataPoint> mResults;
    WiFiAdapter adapter;



    private Boolean registered = false;


    @BindView(R.id.rvWiFi)
    RecyclerView recyclerView;


    public WiFiSignalFragment() {
        // Required empty public constructor
    }

    public static WiFiSignalFragment newInstance() {
        WiFiSignalFragment fragment = new WiFiSignalFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wi_fi_signal, container, false);

        ButterKnife.bind(this, view);

        WifiManager mainWifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mainWifi.startScan();

        // Bind to WiFiService
        Intent wfIntent = new Intent(getActivity(), WiFiService.class);
        getActivity().bindService(wfIntent, mConnection, Context.BIND_AUTO_CREATE);

        Util.scheduleWiFiJob(getActivity());


        // Inflate the layout for this fragment
        return view;
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.v("dzo", "onServiceConnected");

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WiFiService.LocalWiFiBinder binder = (WiFiService.LocalWiFiBinder) service;
            mWifiService = binder.getService();
            mResults = mWifiService.getWiFiInfo();
            mBound = true;

            populateWiFiviews();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    private void populateWiFiviews() {

        adapter = new WiFiAdapter(getActivity(), mResults);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!registered) {

            //assigning two intent filters to one broadcast receiver to monitor updates and scheduled jobs
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.WIFI_BROADCAST);
            intentFilter.addAction(Constants.WIFI_RESULTS);
            getActivity().registerReceiver(mWifiScanReceiver, intentFilter);
            registered = true;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (registered) {
            getActivity().unregisterReceiver(mWifiScanReceiver);
            registered = false;
        }
    }


    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            populateWiFiviews();
            Log.v("danke",intent.getAction());
        }

    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //cancel all JobScheduler jobs
        Util.cancelWiFiJob(getActivity());

        if (unbinder != null) {
            unbinder.unbind();
        }

    }

}
