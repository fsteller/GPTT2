package com.credomatic.grpod.ecommerce.android.gptt.testingtool.webserver.handlers;


import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.AppGPTT;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public class DefaultHandler implements HttpRequestHandler {


    private static final String TAG = DefaultHandler.class.getSimpleName();


    protected AppGPTT context = null;

    public DefaultHandler(final AppGPTT context) {
        this.context = context;
    }

    @Override
    public void handle(final HttpRequest httpRequest, final HttpResponse httpResponse, final HttpContext httpContext) throws HttpException, IOException {

    }
}
