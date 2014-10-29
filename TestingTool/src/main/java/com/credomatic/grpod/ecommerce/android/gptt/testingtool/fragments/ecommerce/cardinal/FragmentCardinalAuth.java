package com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.cardinal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.R;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.AppGPTT;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.ResultDialogFragment;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.net.NetHttp;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.security.Md5;

import org.apache.http.util.EncodingUtils;

import java.net.URLDecoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public class FragmentCardinalAuth extends BaseFragmentCardinal implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //<editor-fold desc="Constants">

    private static final String TAG = FragmentCardinalAuth.class.getSimpleName();
    private final RadioButtonSwitcher mRadioButtonSwitcher = new RadioButtonSwitcher();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private boolean isBrowserRedirect = false;
    private RadioButton browserRedirect = null;
    private RadioButton directPos = null;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public FragmentCardinalAuth() {
    }

    //</editor-fold>

    //<editor-fold desc="Overrides">

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_cardinal_auth, container, false);
        if (rootView != null) {

            directPos = ((RadioButton) rootView.findViewById(R.id.directPos_rb));
            directPos.setOnClickListener(mRadioButtonSwitcher);

            browserRedirect = ((RadioButton) rootView.findViewById(R.id.browserRedirect_rb));
            browserRedirect.setOnClickListener(mRadioButtonSwitcher);

            sendButton = ((Button) rootView.findViewById(R.id.send_button));
            sendButton.setOnClickListener(this);

            country_spinner = (Spinner) rootView.findViewById(R.id.countrySpinner);
            state_spinner = (Spinner) rootView.findViewById(R.id.stateSpinner);
            city_spinner = (Spinner) rootView.findViewById(R.id.citySpinner);

            setupSpinner(country_spinner, R.array.countries, true);
        }

        setupUI(rootView);
        setupType(Types.Auth);

        return rootView;
    }

    @Override
    public void onClick(final View v) {
        final AsyncTask<Void, Void, String> task = isBrowserRedirect ? new BrowserRedirect() : new DirectPos();
        task.execute();
    }

    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {

        city_spinner.setAdapter(null);
        if (parent.equals(country_spinner) && country_spinner.getSelectedItem() != null) {

            state_spinner.setAdapter(null);
            final String state = ((TextView)
                    country_spinner.getSelectedView()).getText().toString();

            if (state.equals("CR")) setupSpinner(state_spinner, R.array.cr_states, true);
            else if (state.equals("US")) setupSpinner(state_spinner, R.array.us_states, true);
            else if (state.equals("PA")) setupSpinner(state_spinner, R.array.pa_states, true);
            else if (state.equals("NI")) setupSpinner(state_spinner, R.array.ni_states, true);
            else if (state.equals("HN")) setupSpinner(state_spinner, R.array.hn_states, true);
            else if (state.equals("GT")) setupSpinner(state_spinner, R.array.gt_states, true);
            else if (state.equals("SV")) setupSpinner(state_spinner, R.array.sv_states, true);

        } else if (parent.equals(state_spinner)) {
            final TextView countryTxt = ((TextView) country_spinner.getSelectedView());
            final TextView stateTxt = ((TextView) state_spinner.getSelectedView());

            if (countryTxt != null && stateTxt != null) {
                final String countryStr = countryTxt.getText().toString();
                final String stateStr = stateTxt.getText().toString();
                setupCities(countryStr, stateStr);
            }
        }

    }

    @Override
    public void onNothingSelected(final AdapterView<?> parent) {

    }

    //</editor-fold>
    //<editor-fold desc="Private">

    private void setupCities(final String country, final String state) {
        if (country.equals("US")) {
            if (state.equals("FL")) {
                setupSpinner(city_spinner, R.array.fl_cities, false);
            }
        } else if (country.equals("CR")) {
            if (state.equals("San Jose"))
                setupSpinner(city_spinner, R.array.san_jose_cities, false);
            else if (state.equals("Heredia"))
                setupSpinner(city_spinner, R.array.heredia_cities, false);
            else if (state.equals("Cartago"))
                setupSpinner(city_spinner, R.array.cartago_cities, false);
            else if (state.equals("Alajuela"))
                setupSpinner(city_spinner, R.array.alajuela_cities, false);
            else if (state.equals("Puntarenas"))
                setupSpinner(city_spinner, R.array.puntarenas_cities, false);
            else if (state.equals("Guanacaste"))
                setupSpinner(city_spinner, R.array.guanacaste_cities, false);
            else if (state.equals("Limon")) setupSpinner(city_spinner, R.array.limon_cities, false);
        }
    }

    private void setupSpinner(final Spinner view, final int arrayResId, final boolean attachListener) {
        if (view != null) {
            final String[] data = getResources().getStringArray(arrayResId);
            final ArrayAdapter<String> mAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.simple_spinner_item, data);

            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            view.setAdapter(mAdapter);

            if (attachListener)
                view.setOnItemSelectedListener(this);
        }
    }

    //</editor-fold>

    //<editor-fold desc="Inner Classes">

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
            params.put("type", "auth");
            params.put("fisrtname", firstName);
            params.put("lastname", lastName);
            params.put("address1", addressLine1);
            params.put("address2", addressLine2);
            params.put("country", country);
            params.put("state", state);
            params.put("city", city);
            params.put("zip", zip);

            return NetHttp.doPost(params, URL);
        }
    }

    private final class BrowserRedirect extends BaseTrxOperations {

        private final String redirect = ((AppGPTT) getActivity().getApplication()).getHttpServerAddress();

        @Override
        public final String validate() {

            if (key.isEmpty())
                return getResources().getString(R.string.emptyKeyError);
            else if (keyId.isEmpty())
                return getResources().getString(R.string.emptyKeyIdError);
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
        public final String sendTrx() {
            final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"));
            final String time = String.valueOf((cal.getTimeInMillis() / 1000));

            final String hash = new Md5().getMd5(orderId + PIPE + amount + PIPE + time + PIPE + key);
            return "hash=" + hash + "&key_id=" + keyId + "&type=auth" +
                    "&time=" + time + "&amount=" + amount + "&orderid=" + orderId +
                    "&processor=" + processor + "&ccnumber=" + ccNumber + "&ccexp=" + expDate + "&cvv=" + cvv +
                    "&redirect=" + redirect + "&firstname=" + firstName + "&lastname=" + lastName +
                    "&address1=" + addressLine1 + "&address2=" + addressLine2 +
                    "&city=" + city + "&state=" + state + "&zip=" + zip + "&country=" + country;
        }

        @Override
        protected final void onPostExecute(final String params) {

            final WebView webView = new WebView(getActivity(), null, android.R.attr.webViewStyle);
            final Dialog alert = new Dialog(getActivity(), R.style.DialogTheme);
            final WebSettings webSettings = webView.getSettings();

            webSettings.setSupportZoom(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setBuiltInZoomControls(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(final WebView view, final SslErrorHandler handler, final SslError error) {
                    handler.proceed();
                }

                @Override
                public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                    final boolean mustFinish = url.startsWith(redirect);
                    if (mustFinish) {
                        alert.dismiss();
                        showResultDialog(getMessage(Uri.parse(url)));
                    } else
                        view.loadUrl(url);
                    return !mustFinish;
                }
            });
            webView.postUrl(URL, EncodingUtils.getBytes(params, "BASE64"));

            alert.setTitle(getResources().getString(R.string.browserRedirectTitle));
            alert.setOnCancelListener(new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialog) {
                    alert.dismiss();
                }
            });
            alert.requestWindowFeature(Window.FEATURE_LEFT_ICON);
            alert.requestWindowFeature(Window.FEATURE_PROGRESS);
            alert.setContentView(webView);
            alert.setCancelable(true);
            alert.show();
        }

        private String getMessage(final Uri uri) {
            String result = "";
            try {
                result += "Response:" + URLDecoder.decode(uri.getQueryParameter("response"), "UTF-8");
                result += "\nResponse Text" + URLDecoder.decode(uri.getQueryParameter("responsetext"), "UTF-8");
                result += "\nResponse Code:" + URLDecoder.decode(uri.getQueryParameter("response_code"), "UTF-8");
                result += "\nCvv Response:" + URLDecoder.decode(uri.getQueryParameter("cvvresponse"), "UTF-8");
                result += "\nTransaction Id:" + URLDecoder.decode(uri.getQueryParameter("transactionid"), "UTF-8");
                result += "\nAuthorization:" + URLDecoder.decode(uri.getQueryParameter("authcode"), "UTF-8");
                result += "\nOrder Id:" + URLDecoder.decode(uri.getQueryParameter("orderid"), "UTF-8");
                result += "\nOutBound Hash:" + URLDecoder.decode(uri.getQueryParameter("hash"), "UTF-8");
                result += "\nTime:" + URLDecoder.decode(uri.getQueryParameter("time"), "UTF-8");
                result += "\nAmount:" + URLDecoder.decode(uri.getQueryParameter("amount"), "UTF-8");
            } catch (final Exception e) {
                Log.e("Error processing response for browser redirect method", e.getMessage(), e);
            }
            return result;
        }
    }

    private class RadioButtonSwitcher implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            isBrowserRedirect = v == browserRedirect;
            browserRedirect.setChecked(isBrowserRedirect);
            directPos.setChecked(!isBrowserRedirect);
            setupIntegrationMethod(isBrowserRedirect);
        }
    }

    //</editor-fold>
}

