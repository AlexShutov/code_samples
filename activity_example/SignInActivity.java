package com.myapp.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.myapp.android.R;
import com.myapp.android.WorkforceApp;
import com.myapp.android.dialogs.CustomProgressDialog;
import com.myapp.android.globals.GlobalConstant;
import com.myapp.android.models.UserData;
import com.myapp.android.preferences.AppPreference;
import com.myapp.android.utils.RestClientUtils;
import com.myapp.android.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private CustomProgressDialog progressDialog;

    private ImageView imgLogo, imgLogoTemp;
    private RelativeLayout layoutContent;
    private EditText edtUsername, edtPassword;

    private boolean hasAnimation = true;
    private boolean hasLoggedIn = false;

    private TextView txtVisitWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hasLoggedIn = !WorkforceApp.getInstance().getToken().equals("");

        if (getIntent().getExtras() != null){
            hasAnimation = getIntent().getExtras().getBoolean("has_anim", true);
        }

        setContentView(R.layout.activity_signin);

        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgLogoTemp = (ImageView) findViewById(R.id.imgLogoTemp);
        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        //edtPassword.setTransformationMethod(new PasswordTransformationMethod());
        edtPassword.setTypeface(Typeface.DEFAULT);
        txtVisitWebsite = (TextView) findViewById(R.id.txtVisitWebsite);
        showVisitWebsiteString();

        findViewById(R.id.btnSignin).setOnClickListener(this);
        findViewById(R.id.btnForgotPassword).setOnClickListener(this);

        checkGCMRegistration();

        InitStorage();

        InitAnimation();


    }

    private void showVisitWebsiteString(){
        SpannableString linkSite = Utils.makeLinkSpanByColor("www.workforce.fm", R.color.gray_color4, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Utils.goToBrowser(SignInActivity.this, "http://www.workforce.fm");
            }
        });
        txtVisitWebsite.append("Visit ");
        txtVisitWebsite.append(linkSite);
        txtVisitWebsite.append(" for more information or to create an account");

        Utils.makeLinksFocusable(txtVisitWebsite);
    }

    /** Initializing local storage folder **/
    private void InitStorage() {
        Utils.CreateWorkDirectories(GlobalConstant.getHomeDirPath(), false);
        Utils.CreateWorkDirectories(GlobalConstant.getTempDirpath(), false);
    }

    private void InitAnimation() {
        if (hasAnimation) {
            layoutContent.setVisibility(View.INVISIBLE);
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(hasLoggedIn && hasAnimation? 2000:1500);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            imgLogo.startAnimation(anim);
            imgLogo.postDelayed(new Runnable() {
                @Override
                public void run() {
                    {
                        if (hasLoggedIn && hasAnimation){
                            Intent intent = new Intent(SignInActivity.this, AppPreference.getPin(SignInActivity.this).equals("")? CreatePinActivity.class:EnterPinActivity.class);
                            startActivity(intent);

                            UserData userData = WorkforceApp.getInstance().getUser(false);

                            Intercom.client().registerIdentifiedUser(new Registration().withUserId(String.valueOf(userData.getUserId())));
                            Map userMap = new HashMap();
                            Map companyData = new HashMap();
                            List companies = new ArrayList();
                            companyData.put("name", userData.getCompanyName());
                            companyData.put("id", userData.getCompanyId());
                            companies.add(companyData);
                            userMap.put("companies", companies);
                            userMap.put("name", userData.getFullName());
                            userMap.put("email", userData.getUserName());
                            Intercom.client().updateUser(userMap);

                            // start location tracking
                            GPSTracker gps = new GPSTracker(SignInActivity.this);
                            if(gps.canGetLocation()){
                                Log.d("juan", "Location enabled");
                            } else {
                                Log.e("juan", "Location disabled");
                            }

                            finish();
                        }else {
                            int dy = (layoutContent.getHeight() - imgLogo.getHeight()) / 2 - getResources().getDimensionPixelSize(R.dimen.signin_top_margin);
                            TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, -1 * dy);
                            anim.setInterpolator(new AccelerateDecelerateInterpolator());
                            anim.setDuration(500);
                            anim.setFillAfter(true);
                            imgLogo.startAnimation(anim);

                            imgLogo.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
                                    anim.setDuration(500);
                                    anim.setInterpolator(new AccelerateDecelerateInterpolator());
                                    layoutContent.startAnimation(anim);

                                    layoutContent.setVisibility(View.VISIBLE);
                                }
                            }, 500);
                        }
                    }
                }
            }, hasLoggedIn && hasAnimation? 2000:1500);
        }else{
            imgLogo.setVisibility(View.GONE);
            imgLogoTemp.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.VISIBLE);
        }
    }

    private void doSignIn(){
        final String username = edtUsername.getText().toString();
        if (username.equals("")){
            showAlertDialog(this, getString(R.string.error), getString(R.string.signin_valid_msg_no_username));
            edtUsername.requestFocus();
            return;
        }

        final String password = edtPassword.getText().toString();
        if (password.equals("")){
            showAlertDialog(this, getString(R.string.error), getString(R.string.signin_valid_msg_no_password));
            edtPassword.requestFocus();
            return;
        }

        /**Added by Prashant Adesara - START */
        if(!Utils.isOnline(SignInActivity.this)){
            showAlertDialog(this, getString(R.string.internet_error_title), getString(R.string.internet_error_msg));
            return;
        }
        /**Added by Prashant Adesara - END*/

        try{
            JSONObject objParams = new JSONObject();
            objParams.put("email", username);
            objParams.put("password", password);
            objParams.put("device_token", AppPreference.getGCMRegId(SignInActivity.this));
            objParams.put("device_type", "Android");
            progressDialog = showProgressDialog(progressDialog, getString(R.string.please_wait));
            RestClientUtils.post(this, getString(R.string.PATH_USER_LOGIN), objParams, false, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(getClass().getName(), "Login response:" + response.toString());
                    if (isActivityActive) {
                        dismissProgressDialog(progressDialog);
                        WorkforceApp.getInstance().setUserInfo(response.toString());

                        AppPreference.setUserId(SignInActivity.this, username);
                        AppPreference.setUserPass(SignInActivity.this, password);

                        // register user for Intercom.io
                        try {
                            Long userId = response.getLong("UserId");

                            Intercom.client().registerIdentifiedUser(new Registration().withUserId(userId.toString()));
                            Map userMap = new HashMap();
                            Map companyData = new HashMap();
                            List companies = new ArrayList();
                            companyData.put("name", response.getString("CompanyName"));
                            companyData.put("id", response.getLong("CompanyId"));
                            companies.add(companyData);
                            userMap.put("companies", companies);
                            userMap.put("name", response.getString("FirstName") + " " + response.getString("LastName"));
                            userMap.put("email", response.getString("UserName"));
                            Intercom.client().updateUser(userMap);


                            // start location tracking
                            GPSTracker gps = new GPSTracker(SignInActivity.this);
                            if(gps.canGetLocation()){
                                Log.d("juan", "Location enabled");
                            } else {
                                Log.e("juan", "Location disabled");
                            }

                        } catch (JSONException exception) {}

                        goToCreatePin();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response:" + responseString);
                    if (isActivityActive) {
                        dismissProgressDialog(progressDialog);
                        showErrorAlert(SignInActivity.this, statusCode);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response:" + obj);
                    if (isActivityActive) {
                        dismissProgressDialog(progressDialog);
                        showErrorAlert(SignInActivity.this, statusCode);
                    }
                }
            });
        }
        catch (JSONException eJSON){}
        catch(Exception e){}
    }

    private void goToForgotPassword(){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.appear_from_right, R.anim.disappear_to_left);
    }

    private void goToCreatePin(){
        Intent intent = new Intent(this, CreatePinActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignin:
                doSignIn();
                break;
            case R.id.btnForgotPassword:
                goToForgotPassword();
                break;
        }
    }
}
