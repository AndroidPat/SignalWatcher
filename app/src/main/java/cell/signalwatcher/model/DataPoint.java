package cell.signalwatcher.model;


/**
 * Model class for the recyclerView containing WiFi information
 */

public class DataPoint {

    private int dataPoint;
    private String extraDataMessage;
    private String imageUrl;
    private String bssId;
    private String signalStrength;

    public DataPoint(int dataPoint, String extraDataMessage, String imageUrl, String bssId, String signalStrength) {
        this.dataPoint = dataPoint;
        this.extraDataMessage = extraDataMessage;
        this.imageUrl = imageUrl;
        this.bssId = bssId;
        this.signalStrength = signalStrength;
    }

    public int getDataPoint() {
        return dataPoint;
    }

    public void setDataPoint(int dataPoint) {
        this.dataPoint = dataPoint;
    }

    public String getExtraDataMessage() {
        return extraDataMessage;
    }

    public void setExtraDataMessage(String extraDataMessage) {
        this.extraDataMessage = extraDataMessage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBssId() {
        return bssId;
    }

    public void setBssId(String bssId) {
        this.bssId = bssId;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }
}
