package com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.net;

import android.util.Log;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public class NetHttp extends NetBase {

    private static final int SOCKET_TIMEOUT = 3000;
    private static final String TAG = NetHttp.class.getSimpleName();
    private static final TrustManager[] TRUST_MANAGER = {new NaiveTrustManager()};
    private static final AllowAllHostnameVerifier HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();

    private NetHttp() {
    }

    // ===================== Public

    /**
     * Send an HTTP(s) request with POST parameters.
     *
     * @param parameters parameters to be sent with the POST request
     * @param url address to be invoked by the request
     */
    public static String doPost(final Map<String, String> parameters, final String url) {

        String result = "Connection successful...\n";
        final URLConnection aux = getConnection(url, "POST");

        if (aux != null && aux instanceof HttpURLConnection) {
            final HttpURLConnection cnx = (HttpURLConnection) aux;
            final String params = formatParameters(parameters);

            try {
                Log.d(TAG, "Sending request to " + url);
                cnx.setFixedLengthStreamingMode(params.length());
                OutputStreamWriter wr = new OutputStreamWriter(cnx.getOutputStream());
                wr.write(params);
                wr.flush();
                wr.close();

                String line;
                Log.d(TAG, "Reading response...");
                BufferedReader rd = new BufferedReader(new InputStreamReader(cnx.getInputStream()));
                for (int lineCount = 0; (line = rd.readLine()) != null; lineCount++)
                    if (lineCount <= 2) {
                        Log.d(TAG, line);
                        result += line + '\n';
                    }
                rd.close();
            } catch (Exception e) {
                return "Error during connection. Please check, and try again...\n" +
                        "Error detail: " + e.getMessage();
            }
        } else
            return "Error during connection. Can't connect to URL: " + url;

        Log.d(TAG, "Response: " + result);
        return result;
    }

    // ===================== private

    private static String formatParameters(final Map<String, String> parameters) {
        final StringBuilder dataBfr = new StringBuilder();
        try {
            for (final String key : parameters.keySet()) {
                if (dataBfr.length() != 0)
                    dataBfr.append('&');

                dataBfr.append(URLEncoder.encode(key, "UTF-8"))
                        .append('=') .append(URLEncoder.encode(parameters.get(key), "UTF-8"));
            }
        } catch (final Exception e) {
            Log.e(TAG, "Posting crash report data: " + e.getMessage());
            return null;
        }
        final String urlParameters = dataBfr.toString().toLowerCase();
        Log.d(TAG, "Request parameters: " + (urlParameters.contains("password") ?
                "Removed by security reasons." : urlParameters));

        return urlParameters;
    }

    /**
     * Open an URL connection. If HTTPS, accepts any certificate even if not
     * valid, and connects to any host name.
     *
     * @param url The destination URL, HTTP or HTTPS.
     * @return The URLConnection.
     */
    private static URLConnection getConnection(final String url, final String method) {

        //System.setProperty("http.keepAlive", "false");
        URLConnection result = null;
        try {

            Log.d(TAG, "URL: " + url);
            final URL urlSpec = new URL(url);
            final URL aux = new URL(urlSpec.getProtocol(), urlSpec.getHost(),
                    urlSpec.getPort(), urlSpec.getFile());

            Log.d(TAG, "Opening connection...");
            result = aux.openConnection();

            if (result instanceof HttpsURLConnection) {
                final SSLContext context = SSLContext.getInstance("TLS");
                context.init(new KeyManager[0], TRUST_MANAGER, new SecureRandom());
                final SSLSocketFactory socketFactory = context.getSocketFactory();
                Log.d(TAG, "Setting connection parameters...");
                ((HttpsURLConnection) result).setHostnameVerifier(HOSTNAME_VERIFIER);
                ((HttpsURLConnection) result).setSSLSocketFactory(socketFactory);
                ((HttpsURLConnection) result).setRequestMethod(method);
            }
            result.setDoOutput(true);
            result.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            result.setConnectTimeout(SOCKET_TIMEOUT);
            result.setReadTimeout(SOCKET_TIMEOUT);
        } catch (Exception e) {
            Log.e(TAG, "Posting crash report data: " + e.getMessage());
        }
        return result;
    }

    private static class NaiveTrustManager implements X509TrustManager {

        /*
        * (non-Javadoc)
        *
        * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
        */
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.
         * X509Certificate[], java.lang.String)
         */
        @Override
        public void checkClientTrusted(X509Certificate[] x509CertificateArray,
                                       String string) throws CertificateException {
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.
         * X509Certificate[], java.lang.String)
         */
        @Override
        public void checkServerTrusted(X509Certificate[] x509CertificateArray,
                                       String string) throws CertificateException {
        }

    }
}
