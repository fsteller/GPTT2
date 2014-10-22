package com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.R;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.IFragmentService;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.cardinal.FragmentCardinalAuth;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.cardinal.FragmentCardinalCapture;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.cardinal.FragmentCardinalSale;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.cardinal.FragmentCardinalVoid;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.paycom.FragmentPaycomAuth;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.paycom.FragmentPaycomSale;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.fragments.ecommerce.paycom.FragmentPaycomVoid;


/**
 * Created by fhernandezs on 09/06/2014.
 */
public class FragmentServiceHeader extends Fragment implements AdapterView.OnItemSelectedListener {
    //<editor-fold desc="Variables">

    private static final int CARDINAL = 0;
    private static final int PAYCOM = 1;
    private static final int PPP = 2;

    private int serviceIndex = -1;

    //</editor-fold>
    //<editor-fold desc="Constructors">

    public FragmentServiceHeader() { }

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_service, container, false);
        serviceIndex = getArguments().getInt(IFragmentService.ARG_SERVICE_ID, -1);

        if (rootView != null && serviceIndex >= 0) {
            getActivity().setTitle(getResources().getStringArray(R.array.services)[serviceIndex]);
            switch (serviceIndex) {
                case CARDINAL:
                    return setupCardinalHeader(rootView);
                case PAYCOM:
                    return setupPaycomHeader(rootView);
                case PPP:
                    return setupPppHeader(rootView);
                default:
            }
        }

        //int imageId = getResources().getIdentifier(Service.toLowerCase(Locale.getDefault()), "drawable", getActivity().getPackageName());
        //((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);

        return rootView;
    }

    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {

        final  Fragment mFragment = getProperServiceFragment(serviceIndex, position);

        if (mFragment != null) {
            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.inner_content_frame, mFragment).commit();
        }
    }

    @Override
    public void onNothingSelected(final AdapterView<?> parent) {

    }

    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private Fragment getProperServiceFragment(final int serviceIndex, final int opt) {
        Fragment output = null;
        switch (serviceIndex) {
            case CARDINAL:
                output = opt == 0 ? new FragmentCardinalAuth() :
                        opt == 1 ? new FragmentCardinalCapture() :
                                opt == 2 ? new FragmentCardinalSale() :
                                        new FragmentCardinalVoid();
                break;
            case PAYCOM:
                output = opt == 0 ? new FragmentPaycomAuth() :
                        opt == 1 ? new FragmentPaycomSale() :
                                new FragmentPaycomVoid();
                break;
            case PPP:
               // output = opt == 0 ? new FragmentPppSftp() :
               //         new FragmentPppFtps();
                break;
        }
        return output;
    }

    private View setupCardinalHeader(final View rootView) {
        setupText((TextView) rootView.findViewById(R.id.header_titleLabel), R.string.headerTitle_cardinal);
        setupText((TextView) rootView.findViewById(R.id.header_descriptionLabel), R.string.headerDescription_cardinal);
        setupText((TextView) rootView.findViewById(R.id.header_spinner_titleLabel), R.string.headerSpinnerTitle_cardinal);
        setupImage((ImageView) rootView.findViewById(R.id.header_imageView), R.drawable.cardinalcommerce);
        setupSpinner((Spinner) rootView.findViewById(R.id.header_spinner), R.array.cardinal_opts);

        return rootView;
    }

    private View setupPaycomHeader(final View rootView) {
        setupText((TextView) rootView.findViewById(R.id.header_titleLabel), R.string.headerTitle_paycom);
        setupText((TextView) rootView.findViewById(R.id.header_descriptionLabel), R.string.headerDescription_paycom);
        setupText((TextView) rootView.findViewById(R.id.header_spinner_titleLabel), R.string.headerSpinnerTitle_paycom);
        setupImage((ImageView) rootView.findViewById(R.id.header_imageView), R.drawable.paycomcommerce);
        setupSpinner((Spinner) rootView.findViewById(R.id.header_spinner), R.array.paycom_opts);
        return rootView;
    }

    private View setupPppHeader(final View rootView) {
        setupText((TextView) rootView.findViewById(R.id.header_titleLabel), R.string.headerTitle_ppp);
        setupText((TextView) rootView.findViewById(R.id.header_descriptionLabel), R.string.headerDescription_ppp);
        setupText((TextView) rootView.findViewById(R.id.header_spinner_titleLabel), R.string.headerSpinnerTitle_ppp);
        //setupImage((ImageView) rootView.findViewById(R.id.header_imageView), R.drawable.headerImage_paycom);
        setupSpinner((Spinner) rootView.findViewById(R.id.header_spinner), R.array.ppp_opts);
        return rootView;
    }

    private void setupText(final TextView view, final int resId) {
        if (view != null) {
            view.setText(getResources().getString(resId));
        }
    }

    private void setupImage(final ImageView view, final int resId) {
        view.setImageResource(resId);
    }

    private void setupSpinner(final Spinner view, final int arrayResId) {
        if (view != null) {
            final String[] data = getResources().getStringArray(arrayResId);
            final ArrayAdapter<String> mAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.simple_spinner_item, data);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            view.setAdapter(mAdapter);
            view.setOnItemSelectedListener(this);
        }
    }

    //</editor-fold>
}
