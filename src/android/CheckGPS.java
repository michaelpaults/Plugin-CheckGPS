package de.fastr.phonegap.plugins;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import android.location.LocationManager;
import android.content.Context;
import android.os.Build;

public class CheckGPS extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("check".equals(action)) {
            this.check(callbackContext);
            return true;
        }
        return false;
    }

    private void check(CallbackContext callbackContext) {
        Context context = this.cordova.getActivity().getApplicationContext();
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm == null) {
            callbackContext.error("LocationManager not available");
            return;
        }

        boolean enabled;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // API 28+
            try {
                enabled = lm.isLocationEnabled();
            } catch (Exception e) {
                enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                          lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } else {
            enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                      lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }

        if (enabled) {
            callbackContext.success();
        } else {
            callbackContext.error("Location services disabled");
        }
    }
}