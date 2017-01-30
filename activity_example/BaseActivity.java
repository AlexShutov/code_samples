package com.myapp.android.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.myapp.android.R;
import com.myapp.android.dialogs.CustomProgressDialog;
import com.myapp.android.globals.GlobalConstant;
import com.myapp.android.preferences.AppPreference;

import java.io.IOException;

public class BaseActivity extends AppCompatActivity {

    protected boolean isActivityActive;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isActivityActive = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityActive = false;
    }

    public void onStop() {
        super.onStop();
        isActivityActive = false;
        System.gc();
    }

    public boolean isActivityStopped() {
        return !isActivityActive;
    }

    protected void showToast(String text, int duration) {
        if(isActivityStopped()) {
            return;
        }

        Toast.makeText(this, text, duration).show();
    }

    public static void showAlertDialog(Context ctx, String title, String sMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom);
        builder.setMessage(sMsg);
        // Set up the buttons
        builder.setTitle(title);
        builder.setPositiveButton(ctx.getResources().getString(R.string.btn_label_ok), null);
        builder.show();
    }

    protected CustomProgressDialog showProgressDialog(CustomProgressDialog dialog, String text) {
        if (dialog == null) {
            dialog = new CustomProgressDialog(this, text);
        } else {
            dialog.setMessage(text);
        }

        if (!dialog.isShowing()) dialog.show();

        return dialog;
    }

    protected void dismissProgressDialog(CustomProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected void showErrorAlert(Context ctx, int statusCode){
        String title = ctx.getResources().getString(R.string.error);
        String msg = ctx.getResources().getString(R.string.error_bad_thing);
        switch(statusCode){
            case 200:
                msg = ctx.getResources().getString(R.string.error_invalid_user);
                break;
            case 401:
                msg = ctx.getResources().getString(R.string.error_invalid_token);
                break;
        }

        showAlertDialog(ctx, title, msg);
    }

    private Context context;
    private GoogleCloudMessaging gcm;
    private String regid = "";
    public void checkGCMRegistration(){
        context = getApplicationContext();
        if( checkPlayServices() ){
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.equals("")) {
                registerInBackground();
            } else {
                Log.i(getClass().getName(), "Registration is found:" + regid);
            }
        }
    }

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(getClass().getName(), "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        String registrationId = AppPreference.getGCMRegId(context);
        if (registrationId.equals("")) {
            Log.i(getClass().getName(), "Registration not found.");
            return "";
        }

        return registrationId;
    }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(GlobalConstant.SENDER_ID);
                    return regid;
                } catch (IOException ex) {
                    Log.e(getClass().getName(), "IOException:" + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String id) {
                if( id != null ) {
                    AppPreference.setGCMRegId(context, id);
                }
            }
        }.execute(null, null, null);
    }
}