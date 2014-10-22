package com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.cardinal;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.R;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.net.NetHttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public class FragmentCardinalSale extends BaseFragmentCardinal implements View.OnClickListener {

    private static final String TAG = FragmentCardinalSale.class.getSimpleName();

    public FragmentCardinalSale() {
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_cardinal_sale, container, false);

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
            else if (password.isEmpty())
                return getResources().getString(R.string.emptyPasswordError);
            else if (ccNumber.isEmpty())
                return getResources().getString(R.string.emptyCcNumbereError);
            else if (expDate.isEmpty())
                return getResources().getString(R.string.emptyExpDateError);
            else if (amount.isEmpty())
                return getResources().getString(R.string.emptyAmountError);
            else if (!NetHttp.hasConnection(getActivity()))
                return getResources().getString(R.string.noInternetConnection);
            else return null;
        }

        @Override
        protected final String sendTrx() {

            final Map<String, String> params = new HashMap<String, String>();

            params.put("username", username);
            params.put("password", password);
            params.put("amount", amount);
            params.put("orderid", orderId);
            params.put("processor_id", processor);
            params.put("ccnumber", ccNumber);
            params.put("ccexp", expDate);
            params.put("cvv", cvv);
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
