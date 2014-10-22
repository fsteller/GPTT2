package com.credomatic.grpod.ecommerce.android.gptt.testingtool.webserver;


import android.util.Log;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.R;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.AppGPTT;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.net.NetHttp;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.webserver.handlers.DefaultHandler;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.webserver.handlers.TrxResponseHandler;

import org.apache.http.HttpException;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public class WebServer extends Thread {

    private static final String TAG = WebServer.class.getSimpleName();
    private static final String SERVER_NAME = "GPTTWS";

    private AppGPTT context = null;

    private int port = -1;
    private boolean isRunning = false;
    private BasicHttpContext httpContext = null;
    private HttpService httpService = null;

    public WebServer(final AppGPTT app) {
        super(SERVER_NAME);

        this.context = app;

        this.port = context.getHttpServerPort();
        final String trxResponsePattern = app.getResources().getString(R.string.trxResponsePattern);
        final String allPattern = app.getResources().getString(R.string.allPattern);

        final BasicHttpProcessor httpProcessor = new BasicHttpProcessor();
        httpContext = new BasicHttpContext();

        httpProcessor.addInterceptor(new ResponseDate());
        httpProcessor.addInterceptor(new ResponseServer());
        httpProcessor.addInterceptor(new ResponseContent());
        httpProcessor.addInterceptor(new ResponseConnControl());

        httpService = new HttpService(httpProcessor,
                new DefaultConnectionReuseStrategy(),
                new DefaultHttpResponseFactory());

        final HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
        registry.register(trxResponsePattern, new TrxResponseHandler(context));
        registry.register(allPattern, new DefaultHandler(context));

        httpService.setHandlerResolver(registry);
    }

    @Override
    public void run() {
        super.run();

        try {
            final ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

            while (isRunning) {
                try {
                    final Socket socket = serverSocket.accept();
                    final DefaultHttpServerConnection serverConnection = new DefaultHttpServerConnection();

                    serverConnection.bind(socket, new BasicHttpParams());
                    httpService.handleRequest(serverConnection, httpContext);
                    serverConnection.shutdown();

                } catch (final IOException e) {
                    Log.e(TAG, "Error: " + e.getMessage());
                    e.printStackTrace();
                } catch (final HttpException e) {
                    Log.e(TAG, "Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            serverSocket.close();
        } catch (final IOException e) {
            Log.e(TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void startThread() {
        super.start();
        isRunning = true;
        Log.i(TAG, "Thread " + SERVER_NAME + " started.");
    }

    public synchronized void stopThread() {
        isRunning = false;
        Log.i(TAG, "Thread " + SERVER_NAME + " stopped.");
    }

    public String getServerAddress() {
        return "http://" + NetHttp.getLocalIpV4Address() + ":" + context.getHttpServerPort();
    }

    public boolean isRunning(){
        return isRunning;
    }
}
