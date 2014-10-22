package com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.paycom;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.R;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.net.NetHttp;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.security.Md5;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public class FragmentPaycomSale  extends BaseFragmentPaycom implements View.OnClickListener  {
    private static final String TAG = FragmentPaycomSale.class.getSimpleName();

    public FragmentPaycomSale() {
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_paycom_sale, container, false);

        if (rootView != null) {
            sendButton = ((Button) rootView.findViewById(R.id.send_button));
            sendButton.setOnClickListener(this);
        }

        setupUI(rootView);
        setupType(Types.Sale);

        return rootView;
    }

    @Override
    public void onClick(final View v) {
        new DirectPos().execute();
    }

    private final class DirectPos extends BaseTrxOperations {

        @Override
        protected final String validate() {
            if (username.isEmpty())
                return getResources().getString(R.string.emptyUsernameError);
            else if (key.isEmpty())
                return getResources().getString(R.string.emptyKeyError);
            else if (keyId.isEmpty())
                return getResources().getString(R.string.emptyKeyIdError);
            else if (trxId.isEmpty())
                return getResources().getString(R.string.emptyTrxIdError);
            else if (!NetHttp.hasConnection(getActivity()))
                return getResources().getString(R.string.noInternetConnection);
            else return null;
        }

        @Override
        protected final String sendTrx() {

            final Map<String, String> params = new HashMap<String, String>();
            final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"));
            final String time = String.valueOf((cal.getTimeInMillis() / 1000));

            final String hash = new Md5().getMd5(orderId + PIPE + amount + PIPE + time + PIPE + key);


            params.put("time", time);
            params.put("hash", hash);
            params.put("key_id", keyId);
            params.put("username", username);
            params.put("transaction_id", trxId);
            params.put("type", "sale");

            return NetHttp.doPost(params, URL);
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            Log.i(TAG, "DirectPOS result: " + result);
            //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        }
    }
}
