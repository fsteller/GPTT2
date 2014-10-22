package com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.R;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public class ResultDialogFragment extends DialogFragment{
    private String result = "";

    public ResultDialogFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.dialogfragment_response, container);
        if (view != null) {
            String dialogText = "";
            getDialog().setTitle("Result");

            final TextView resultDialogText = (TextView) view.findViewById(R.id.resultDialogText);
            final String[] params = result.split("&");
            for (final String s : params)
                dialogText += s + '\n';
            resultDialogText.setText(dialogText);
        }
        return view;
    }

    public void setResultDialogText(final String text) {
        result = text;
    }
}
