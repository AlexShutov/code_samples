import android.content.Context;

import com.myapp.model.WiFiInfo;

/**
 * General class which utilizes methods for obtaining  info about current network
 */
public interface NetworkInfoProvider {

    /**
     * Provides information about wi-fi connection
     * @param c instance of {@link Context}
     * @return {@link WiFiInfo} in successful case,
     * {@link WiFiInfo} with empty values in case of error
     */
    WiFiInfo getWiFiInfo(Context c);

    /**
     * Provides IP address in Local Wi-Fi network
     * @param c instance of {@link Context}
     * @return String representation of IP address or empty string in error case
     */
    String getIPAddresses(Context c);

    /**
     * Provides MAC address in Local Wi-Fi netowrk
     * @param c instance of {@link Context}
     * @return
     */
    String getMacAddresses(Context c);

    /**
     * Provides Gateway in local Wi-Fi network
     * @param c instance of {@link Context}
     * @return
     */
    String getLanGateway(Context c);

    /**
     * Provides bandwidth
     * @param c
     * @return
     */
    String getWanBandwidth(Context c);
}