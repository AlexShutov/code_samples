package com.myapp.android.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.myapp.android.R;
import com.myapp.android.WorkforceApp;
import com.myapp.android.dialogs.CustomProgressDialog;
import com.myapp.android.utils.RestClientUtils;
import com.myapp.android.utils.Utils;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {

    private CustomProgressDialog progressDialog;

    private Toolbar toolbar;
    private EditText edtEmailAddress;

    private LinearLayout layoutForgotPassContent;
    private LinearLayout layoutPassSentContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edtEmailAddress = (EditText) findViewById(R.id.edtEmailAddress);
        layoutForgotPassContent = (LinearLayout) findViewById(R.id.layoutForgotPassContent);
        layoutPassSentContent = (LinearLayout) findViewById(R.id.layoutPassSentContent);

        layoutForgotPassContent.setVisibility(View.VISIBLE);
        layoutPassSentContent.setVisibility(View.GONE);

        findViewById(R.id.btnSubmit).setOnClickListener(this);
        findViewById(R.id.btnSignin).setOnClickListener(this);
    }

    private void doSubmit(){
        String emailAddress = edtEmailAddress.getText().toString();

        if (emailAddress.equals("") || !Utils.isEmailValid(emailAddress)){
            showAlertDialog(this, getString(R.string.error), getString(R.string.forgot_password_valid_msg_enter_valid_email));
            edtEmailAddress.requestFocus();
            return;
        }

        try{
            JSONObject objParams = new JSONObject();
            objParams.put("email", emailAddress);
            progressDialog = showProgressDialog(progressDialog, getString(R.string.please_wait));
            RestClientUtils.post(this, getString(R.string.PATH_FORGOT_PASSWORD), objParams, false, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(getClass().getName(), "Login response:" + response.toString());
                    if (isActivityActive) {
                        dismissProgressDialog(progressDialog);

                        layoutForgotPassContent.setVisibility(View.GONE);
                        layoutPassSentContent.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {
                    Log.e(getClass().getName(), "Failed1, code:" + statusCode + ", response:" + responseString);
                    if (isActivityActive) {
                        dismissProgressDialog(progressDialog);
                        showErrorAlert(ForgotPasswordActivity.this, statusCode);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                    Log.e(getClass().getName(), "Failed2, code:" + statusCode + ", response:" + obj);
                    if (isActivityActive) {
                        dismissProgressDialog(progressDialog);
                        showErrorAlert(ForgotPasswordActivity.this, statusCode);
                    }
                }
            });
        }catch(Exception e){}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubmit:
                doSubmit();
                break;
            case R.id.btnSignin:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.appear_from_left, R.anim.disappear_to_right);
    }
}
