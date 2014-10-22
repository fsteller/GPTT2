package com.credomatic.grpod.ecommerce.android.gptt.testingtool.webserver.handlers;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.R;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.activityGPTT;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.AppGPTT;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.net.NetHttp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public class TrxResponseHandler extends DefaultHandler {

    private static final int NOTIFICATION_ID = 0;
    private static final String TAG = TrxResponseHandler.class.getSimpleName();

    public TrxResponseHandler(AppGPTT context) {
        super(context);
    }

    @Override
    public void handle(final HttpRequest httpRequest, final HttpResponse httpResponse, final HttpContext httpContext) throws HttpException, IOException {
        final Uri uri = Uri.parse(httpRequest.getRequestLine().getUri());
        final String resp = getMessage(uri, context);
        final String msg = context.getResources().getString(R.string.notification_responsehandler_msg);// + ": " + resp;
        //final HttpEntity entity = new EntityTemplate(new ContentProducer() {
        //    public void writeTo(final OutputStream outStream) throws IOException {
        //        final OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        //        writer.write(resp);
        //        writer.flush();
        //    }
        //});

        Log.i(TAG, "Message URI: " + uri.toString());
        context.sendNotificationMessage(msg, NOTIFICATION_ID,
                Notification.FLAG_ONLY_ALERT_ONCE |
                Notification.FLAG_FOREGROUND_SERVICE |
                Notification.FLAG_AUTO_CANCEL);

        //httpResponse.setHeader("Content-Type", "text/html");
        //httpResponse.setEntity(entity);

        final Intent intent = new Intent(activityGPTT.HttpResponseBroadcastReceiver.BrowserRedirectBroadcast);
        intent.putExtra(activityGPTT.HttpResponseBroadcastReceiver.HttpResponseText, resp);
        context.sendBroadcast(intent);

        //httpContext.--
    }

    private static String getMessage(final Uri uri, final Context context) throws UnsupportedEncodingException {

        String html = "";
        final String htmlString = NetHttp.openHTMLString(context, R.raw.transaction_response);
        final List<BasicNameValuePair> values = new ArrayList<BasicNameValuePair>();

        values.add(new BasicNameValuePair("Response:", URLDecoder.decode(uri.getQueryParameter("response"), "UTF-8")));
        values.add(new BasicNameValuePair("Response Text", URLDecoder.decode(uri.getQueryParameter("responsetext"), "UTF-8")));
        values.add(new BasicNameValuePair("Response Code:", URLDecoder.decode(uri.getQueryParameter("response_code"), "UTF-8")));
        values.add(new BasicNameValuePair("Cvv Response:", URLDecoder.decode(uri.getQueryParameter("cvvresponse"), "UTF-8")));
        values.add(new BasicNameValuePair("Transaction Id:", URLDecoder.decode(uri.getQueryParameter("transactionid"), "UTF-8")));
        values.add(new BasicNameValuePair("Authorization:", URLDecoder.decode(uri.getQueryParameter("authcode"), "UTF-8")));
        values.add(new BasicNameValuePair("Order Id:", URLDecoder.decode(uri.getQueryParameter("orderid"), "UTF-8")));
        values.add(new BasicNameValuePair("OutBound Hash:", URLDecoder.decode(uri.getQueryParameter("hash"), "UTF-8")));
        values.add(new BasicNameValuePair("Time:", URLDecoder.decode(uri.getQueryParameter("time"), "UTF-8")));
        values.add(new BasicNameValuePair("Amount:", URLDecoder.decode(uri.getQueryParameter("amount"), "UTF-8")));

        for (BasicNameValuePair v : values) {
            html += "<tr>";
            html += "<td>" + v.getName() + "<br></td>";
            html += "<td>" + v.getValue() + "</td>";
            html += "</tr>";
        }
        html = htmlString.replace("%VALUES%", html);

        return html;
    }
}
