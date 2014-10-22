package com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.paycom;


import android.view.View;
import android.widget.Button;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.BaseFragmentEcommerce;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public abstract class BaseFragmentPaycom extends BaseFragmentEcommerce {

    protected static enum Types {Auth, Sale, Void}
    protected static final String URL = "https://credomatic.compassmerchantsolutions.com/api/transact.php";
    protected Button sendButton = null;

    @Override
    public final void restoreDefaultValues() {
        key = "w6m5FYDq1iHQPl9YV6XekCT3SZOzSY8R";
        keyId = "49338953";
        amount = "1.00";
        orderId = "Mobile Test";
        username = "fagudelo002";
        password = "";
        processor = "";
        ccNumber = "4012001011000771";
        expDate = "1222";
        cvv = "123";
        trxId = "";
    }

    protected final void setupType(final Types type) {
        final int newTrx = type == Types.Auth ? View.VISIBLE : View.GONE;
        final int updateTrx = type == Types.Auth ? View.GONE : View.VISIBLE;

        trxId_editText.setVisibility(updateTrx);
        processor_editText.setVisibility(newTrx);
        ccNumber_editText.setVisibility(newTrx);
        orderId_editText.setVisibility(newTrx);
        expDate_editText.setVisibility(newTrx);
        cvv_editText.setVisibility(newTrx);

        trxId_label.setVisibility(updateTrx);
        processor_label.setVisibility(newTrx);
        ccNumber_label.setVisibility(newTrx);
        orderId_label.setVisibility(newTrx);
        expDate_label.setVisibility(newTrx);
        cvv_label.setVisibility(newTrx);

        password_editText.setEnabled(false);
    }
}
