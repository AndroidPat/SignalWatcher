package cell.signalwatcher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cell.signalwatcher.service.CellService;

/**
 * Broadcast Receiver that starts when the device is rebooted to relaunch the CellService
 * containing the listeners and file logging
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, CellService.class));
    }
}
