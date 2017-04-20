package cell.signalwatcher;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class WiFiSignalFragment extends Fragment {


    public WiFiSignalFragment() {
        // Required empty public constructor
    }

    public static WiFiSignalFragment newInstance(){
        WiFiSignalFragment fragment = new WiFiSignalFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wi_fi_signal, container, false);
    }

}
