package com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.net;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by fhernandezs on 09/06/2014.
 */
abstract class NetBase {

    private static final String TAG = NetBase.class.getSimpleName();

    /**
     * Checks if the device has Internet connection.
     * @param activity activity required to get the app context
     * @return <code>true</code> if the phone is connected to the Internet.
     */
    public static boolean hasConnection(final Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getBaseContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            return ((activeNetwork != null && activeNetwork.isConnected()) ||
                    (mobileNetwork != null && mobileNetwork.isConnected()) ||
                    (wifiNetwork != null && wifiNetwork.isConnected()));

        } catch (final Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static String getLocalIpV6Address() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getLocalIpV4Address() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress()))
                        return inetAddress.getHostAddress();
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

    public static String convertStreamToString(final InputStream is) {
        /*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }

            return writer.toString();
        } else {
            return "";
        }
    }

    public static String openHTMLString(final Context context, final int id) {
        InputStream is = context.getResources().openRawResource(id);

        return convertStreamToString(is);
    }
}
