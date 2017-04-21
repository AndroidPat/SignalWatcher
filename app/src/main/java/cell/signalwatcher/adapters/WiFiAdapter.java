package cell.signalwatcher.adapters;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cell.signalwatcher.R;

public class WiFiAdapter extends RecyclerView.Adapter<WiFiAdapter.ViewHolder> {

    public static TextView wiFiTextView;
    List<ScanResult> mResults;

    private Context mContext;

    // Pass in the contact array into the constructor
    public WiFiAdapter(Context context, List<ScanResult> results) {
        mResults = results;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
            wiFiTextView = (TextView) itemView.findViewById(R.id.listTitle);
        }
    }


    @Override
    public WiFiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.rv_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WiFiAdapter.ViewHolder holder, int position) {
        ScanResult result = mResults.get(position);
        wiFiTextView.setText(result.SSID);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }
}
