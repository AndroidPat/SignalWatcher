package cell.signalwatcher;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cell.signalwatcher.adapters.WiFiAdapter;
import cell.signalwatcher.service.WiFiService;


/**
 * A simple {@link Fragment} subclass.
 */
public class WiFiSignalFragment extends Fragment {

    WiFiService mWifiService;
    boolean mBound = false;
    private Unbinder unbinder;
    private List<ScanResult> mResults;
    WiFiAdapter adapter;


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

        // Bind to WiFiService
        Intent wfIntent = new Intent(getActivity(), WiFiService.class);
        getActivity().bindService(wfIntent, mConnection, Context.BIND_AUTO_CREATE);


        // Inflate the layout for this fragment
        return view;
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WiFiService.LocalWiFiBinder binder = (WiFiService.LocalWiFiBinder) service;
            mWifiService = binder.getService();
            mResults = mWifiService.getWiFiInfo();
            mBound = true;
            adapter = new WiFiAdapter(getActivity(), mResults);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }

    }

}
