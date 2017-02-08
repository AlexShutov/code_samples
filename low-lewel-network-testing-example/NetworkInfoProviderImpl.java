
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.myapp.AiroNetworkInfoProvider;
import com.myapp.Const;
import com.myapp.Utils;
import com.myapp.model.WiFiInfo;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * {@inheritDoc}
 */
public class NetworkInfoProviderImpl implements NetworkInfoProvider {

    /**
     * {@inheritDoc}
     */
    public WiFiInfo getWiFiInfo(Context c){
        if(!hasConnection(c)){
            return new WiFiInfo("", "");
        }
        WifiManager wiFiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo =
                wiFiManager.getConnectionInfo();
        String ssid = connectionInfo.getSSID();
        String bssid = connectionInfo.getBSSID();
        return new WiFiInfo((ssid == null || ssid.equals(Const.UNKNOWN_SSID)) ? Const.EMPTY_STRING : ssid,
                (bssid == null) ? Const.EMPTY_STRING : bssid);
    }

    /**
     * {@inheritDoc}
     */
    public String getIPAddresses(Context c){
        if(!hasConnection(c)){
            return "";
        }
        WifiManager wifiManager = (WifiManager) c.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            return "";
        }

        return ipAddressString;
    }

    /**
     * {@inheritDoc}
     */
    public String getMacAddresses(Context c){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return getMacAddressFromWiFiManager(c);
        } else {
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(Integer.toHexString(b & 0xFF) + ":");
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            } catch (Exception ex) {
                return getMacAddressFromWiFiManager(c);
            }
            return getMacAddressFromWiFiManager(c);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLanGateway(Context c) {
        WifiManager wifiManager = (WifiManager) c.getSystemService(WIFI_SERVICE);
        return Utils.intToIp(wifiManager.getDhcpInfo().gateway);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWanBandwidth(Context c) {
        WifiManager wifiManager = (WifiManager) c.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        Integer linkSpeed = 0;
        if (wifiInfo != null) {
            linkSpeed = wifiInfo.getLinkSpeed(); //measured using WifiInfo.LINK_SPEED_UNITS
        }

        return String.valueOf(linkSpeed);
    }

    /**
     * Obtains MAC address from wi-fi manager ervice. It will work only on devices < Android 6.0
     * on devices >= Android 6.0 it always will return default MAC. For such devices use {@link #getMacAddresses(Context)}
     * @param c
     * @return MAC address or stub "02:00:00:00:00:00"
     */
    private String getMacAddressFromWiFiManager(Context c){
        WifiManager wifiManager = (WifiManager) c.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInf = wifiManager.getConnectionInfo();
        return wifiInf.getMacAddress();
    }

    private boolean hasConnection(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!networkInfo.isConnected()) {
            return false;
        }
        return true;
    }
}
