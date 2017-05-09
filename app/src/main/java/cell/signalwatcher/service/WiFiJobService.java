package cell.signalwatcher.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import cell.signalwatcher.Utils.Constants;
import cell.signalwatcher.Utils.Util;

/**
 * JobService class initiating sending Broadcasts at predefined intervals
 */


public class WiFiJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {

        Intent i = new Intent(Constants.WIFI_BROADCAST);
        sendBroadcast(i);

        //scheduling another job (recurring task - specified intervals)
        Util.scheduleWiFiJob(getApplicationContext());

        //returning false as the processing can happen on the main thread because of the simplicity of the job
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
