package com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.R;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.IActivity;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.IFragmentService;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public abstract class BaseFragmentEcommerce extends Fragment implements IFragmentService {

    private ProgressDialog pd = null;

    protected View key_label = null;
    protected View keyId_label = null;
    protected View userName_label = null;
    protected View password_label = null;
    protected View processor_label = null;
    protected View ccNumber_label = null;
    protected View orderId_label = null;
    protected View expDate_label = null;
    protected View amount_label = null;
    protected View trxId_label = null;
    protected View cvv_label = null;

    protected EditText key_editText = null;
    protected EditText keyId_editText = null;
    protected EditText userName_editText = null;
    protected EditText password_editText = null;
    protected EditText processor_editText = null;
    protected EditText ccNumber_editText = null;
    protected EditText orderId_editText = null;
    protected EditText expDate_editText = null;
    protected EditText amount_editText = null;
    protected EditText trxId_editText = null;
    protected EditText cvv_editText = null;

    protected EditText firstName_editText = null;
    protected EditText lastName_editText = null;
    protected EditText addressLine1_editText = null;
    protected EditText addressLine2_editText = null;
    protected EditText zip_editText = null;

    protected Spinner country_spinner = null;
    protected Spinner state_spinner = null;
    protected Spinner city_spinner = null;

    protected String key = "";
    protected String keyId = "";
    protected String amount = "";
    protected String orderId = "";
    protected String username = "";
    protected String password = "";
    protected String processor = "";
    protected String ccNumber = "";
    protected String expDate = "";
    protected String cvv = "";
    protected String trxId = "";
    protected String firstName = "";
    protected String lastName = "";
    protected String addressLine1 = "";
    protected String addressLine2 = "";
    protected String city = "";
    protected String state = "";
    protected String zip = "";
    protected String country = "";

    @Override
    public void onResume() {
        super.onResume();
        //((IActivity) getActivity()).mapNewServiceFragment(this);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("key", key);
        outState.putString("keyId", keyId);
        outState.putString("amount", amount);
        outState.putString("orderId", orderId);
        outState.putString("username", username);
        outState.putString("password", password);
        outState.putString("processor", processor);
        outState.putString("ccNumber", ccNumber);
        outState.putString("expDate", expDate);
        outState.putString("cvv", cvv);
    }

    @Override
    public void onViewStateRestored(final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            key = savedInstanceState.getString("key", "53ZdjXS3g4ASw9Jatf885nqkTnJT83s7");
            keyId = savedInstanceState.getString("keyId", "3905547");
            amount = savedInstanceState.getString("amount", "1.00");
            orderId = savedInstanceState.getString("orderId", "MobileEcommerceTest");
            username = savedInstanceState.getString("username", "monitorweb");
            password = savedInstanceState.getString("password", "monitorweb01");
            processor = savedInstanceState.getString("processor", "600010061");
            ccNumber = savedInstanceState.getString("ccNumber", "4012001011000771");
            expDate = savedInstanceState.getString("expDate", "1222");
            cvv = savedInstanceState.getString("cvv", "123");
        } else
            restoreDefaultValues();

        setupData();
    }

    @Override
    public void reloadValues() {
        setupData();
    }

    protected final void setupData() {
        key_editText.setText(key);
        keyId_editText.setText(keyId);
        userName_editText.setText(username);
        password_editText.setText(password);
        processor_editText.setText(processor);
        ccNumber_editText.setText(ccNumber);
        orderId_editText.setText(orderId);
        expDate_editText.setText(expDate);
        amount_editText.setText(amount);
        cvv_editText.setText(cvv);

        if (firstName_editText != null) {
            firstName_editText.setText(firstName);
            lastName_editText.setText(lastName);
            addressLine1_editText.setText(addressLine1);
            addressLine2_editText.setText(addressLine2);
            zip_editText.setText(zip);
        }
    }

    protected final void setupUI(final View rootView) {
        if (rootView != null) {
            key_label = rootView.findViewById(R.id.key_label);
            keyId_label = rootView.findViewById(R.id.keyId_label);
            userName_label = rootView.findViewById(R.id.userName_label);
            password_label = rootView.findViewById(R.id.password_label);
            processor_label = rootView.findViewById(R.id.processor_label);
            ccNumber_label = rootView.findViewById(R.id.ccNumber_label);
            orderId_label = rootView.findViewById(R.id.orderId_label);
            expDate_label = rootView.findViewById(R.id.expDate_label);
            amount_label = rootView.findViewById(R.id.amount_label);
            trxId_label = rootView.findViewById(R.id.trxId_label);
            cvv_label = rootView.findViewById(R.id.cvv_label);

            key_editText = (EditText) rootView.findViewById(R.id.key_editText);
            keyId_editText = (EditText) rootView.findViewById(R.id.keyId_editText);
            userName_editText = (EditText) rootView.findViewById(R.id.userName_editText);
            password_editText = (EditText) rootView.findViewById(R.id.password_editText);
            processor_editText = (EditText) rootView.findViewById(R.id.processor_editText);
            ccNumber_editText = (EditText) rootView.findViewById(R.id.ccNumber_editText);
            orderId_editText = (EditText) rootView.findViewById(R.id.orderId_editText);
            expDate_editText = (EditText) rootView.findViewById(R.id.expiration_editText);
            amount_editText = (EditText) rootView.findViewById(R.id.amount_editText);
            trxId_editText = (EditText) rootView.findViewById(R.id.trxId_editText);
            cvv_editText = (EditText) rootView.findViewById(R.id.cvv_editText);

            firstName_editText = (EditText) rootView.findViewById(R.id.firstNameText);
            lastName_editText = (EditText) rootView.findViewById(R.id.lastNameText);
            addressLine1_editText = (EditText) rootView.findViewById(R.id.addressLine1Text);
            addressLine2_editText = (EditText) rootView.findViewById(R.id.addressLine2Text);
            zip_editText = (EditText) rootView.findViewById(R.id.zipCodeText);

            setupIntegrationMethod(false);
        }
    }

    protected final void setupIntegrationMethod(final boolean isBrowserRedirect) {

        final int br = isBrowserRedirect ? View.VISIBLE : View.GONE;
        final int dp = !isBrowserRedirect ? View.VISIBLE : View.GONE;

        userName_label.setVisibility(dp);
        password_label.setVisibility(dp);
        userName_editText.setVisibility(dp);
        password_editText.setVisibility(dp);

        key_label.setVisibility(br);
        keyId_label.setVisibility(br);
        key_editText.setVisibility(br);
        keyId_editText.setVisibility(br);

    }

    protected abstract class BaseTrxOperations extends AsyncTask<Void, Void, String> {

        protected final static String PIPE = "|";

        protected void loadData() {

            key = key_editText.getText() != null ? key_editText.getText().toString().trim() : "";
            keyId = keyId_editText.getText() != null ? keyId_editText.getText().toString().trim() : "";
            amount = amount_editText.getText() != null ? amount_editText.getText().toString().trim() : "";
            orderId = orderId_editText.getText() != null ? orderId_editText.getText().toString().trim() : "";
            username = userName_editText.getText() != null ? userName_editText.getText().toString().trim() : "";
            password = password_editText.getText() != null ? password_editText.getText().toString().trim() : "";
            processor = processor_editText.getText() != null ? processor_editText.getText().toString().trim() : "";
            ccNumber = ccNumber_editText.getText() != null ? ccNumber_editText.getText().toString().trim() : "";
            expDate = expDate_editText.getText() != null ? expDate_editText.getText().toString().trim() : "";
            cvv = cvv_editText.getText() != null ? cvv_editText.getText().toString().trim() : "";
            trxId = trxId_editText.getText() != null ? trxId_editText.getText().toString().trim() : "";

            firstName = firstName_editText != null && firstName_editText.getText() != null ? firstName_editText.getText().toString().trim() : "";
            lastName = lastName_editText != null && lastName_editText.getText() != null ? lastName_editText.getText().toString().trim() : "";
            addressLine1 = addressLine1_editText != null && addressLine1_editText.getText() != null ? addressLine1_editText.getText().toString().trim() : "";
            addressLine2 = addressLine2_editText != null && addressLine2_editText.getText() != null ? addressLine2_editText.getText().toString().trim() : "";
            zip = zip_editText != null && zip_editText.getText() != null ? zip_editText.getText().toString().trim() : "";

            country = country_spinner != null && country_spinner.getSelectedView() != null ? ((TextView) country_spinner.getSelectedView()).getText().toString() : "";
            state = state_spinner != null && state_spinner.getSelectedView() != null ? ((TextView) state_spinner.getSelectedView()).getText().toString() : "";
            city = city_spinner != null && city_spinner.getSelectedView() != null ? ((TextView) city_spinner.getSelectedView()).getText().toString() : "";
        }

        protected abstract String validate();

        protected abstract String sendTrx();

        protected final void showResultDialog(final String result){
            if(pd != null && pd.isShowing())
                pd.dismiss();

            final ResultDialogFragment rDF = new ResultDialogFragment();
            rDF.setRetainInstance(true);
            rDF.show(getFragmentManager(), "none");
            rDF.setResultDialogText(result);

            Log.i(this.getClass().getSimpleName(), "DirectPOS result: " + result);
        }

        @Override
        protected final void onPreExecute() {
            pd = ProgressDialog.show(getActivity(), "Executing", "Please Wait...");
            loadData();
        }

        @Override
        protected final String doInBackground(final Void... noParams) {
            final String resultMessage = validate();
            return isNullOrEmpty(resultMessage) ? sendTrx() : resultMessage;
        }

        @Override
        protected void onPostExecute(final String result) {
            showResultDialog(result);
        }

    }

    protected static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }
}
