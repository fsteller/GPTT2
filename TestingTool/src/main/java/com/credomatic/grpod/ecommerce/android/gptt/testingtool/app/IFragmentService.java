package com.credomatic.grpod.ecommerce.android.gptt.testingtool.app;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public interface IFragmentService {

    public static final String ARG_SERVICE_ID = "service_id";

    public abstract void restoreDefaultValues();

    public abstract void reloadValues();
}
