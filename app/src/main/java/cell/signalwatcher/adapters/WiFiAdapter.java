package cell.signalwatcher.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cell.signalwatcher.R;
import cell.signalwatcher.model.DataPoint;

/**
 * RecyclerView adapter for the WiFi fragment
 */
public class WiFiAdapter extends RecyclerView.Adapter<WiFiAdapter.ViewHolder> {

    private TextView wiFiTextView, bssidTextView, sigStrengthTextView;
    private ImageView listIcon;
    private ArrayList<DataPoint> mResults;


    private Context mContext;

    // Pass in the contact array into the constructor
    public WiFiAdapter(Context context, ArrayList<DataPoint> results) {
        mResults = results;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }


    class ViewHolder extends RecyclerView.ViewHolder {


        ViewHolder(View itemView) {
            super(itemView);
            wiFiTextView = (TextView) itemView.findViewById(R.id.listTitle);
            bssidTextView = (TextView) itemView.findViewById(R.id.tvBSSID);
            sigStrengthTextView = (TextView) itemView.findViewById(R.id.tvLevel);
            listIcon = (ImageView) itemView.findViewById(R.id.listIcon);

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

        String link = mResults.get(position).getImageUrl();
        String SSID = mResults.get(position).getExtraDataMessage();
        String BSSID = mResults.get(position).getBssId();
        String signalStrength = mResults.get(position).getSignalStrength();
        wiFiTextView.setText(SSID);
        bssidTextView.setText(BSSID);
        Glide.with(mContext).load(link).into(listIcon);
        sigStrengthTextView.setText(signalStrength);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }
}
