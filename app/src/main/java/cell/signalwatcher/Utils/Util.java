package cell.signalwatcher.Utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import cell.signalwatcher.service.WiFiJobService;

public class Util {

    private static JobScheduler wifiJobScheduler;

    // schedule the start of the service every 25 - 30 seconds
    public static void scheduleWiFiJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, WiFiJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(25000); // wait at least 25 sec
        builder.setOverrideDeadline(30000); // maximum delay 30 sec

        wifiJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        wifiJobScheduler.schedule(builder.build());
    }

    public static void cancelWiFiJob(Context context) {
        wifiJobScheduler.cancelAll();
    }

    public static String getWiFiIcon(int strength) {
        String link = Constants.WIFI_LVL_1;
        switch (strength) {
            case 1:
                link = Constants.WIFI_LVL_1;
                break;
            case 2:
                link = Constants.WIFI_LVL_2;
                break;
            case 3:
                link = Constants.WIFI_LVL_3;
                break;
            case 4:
                link = Constants.WIFI_LVL_4;
                break;
            case 5:
                link = Constants.WIFI_LVL_5;
                break;
        }
        return link;
    }
}
