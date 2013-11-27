package net.openfiretechnologies.veloxcontrol.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 0x337 on 21.10.13.
 */
public class ConnectionDetector {

    private final Context mContext;

    public ConnectionDetector(Context context) {
        this.mContext = context;
    }

    /**
     * Checks if Device is connected to Internet
     *
     * @return <b>true</b> if device is connected<br />
     * <b>false</b> if device is not connected
     */
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
}
