package com.myapp.android.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.myapp.android.R;
import com.myapp.android.dialogs.CustomProgressDialog;
import com.myapp.android.globals.GlobalConstant;
import com.myapp.android.models.DocumentData;
import com.myapp.android.models.JobData;
import com.myapp.android.utils.RestClientUtils;
import com.myapp.android.utils.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class SignatureActivity extends BaseActivity implements View.OnClickListener {
    private CustomProgressDialog progressDialog;

    private JobData jobData;

    private TextView txtTitle;
    private SignaturePad mSignaturePad;
    private TextView txtDate;
    private EditText edtName;

    private boolean isSigned = false;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        jobData = (JobData) getIntent().getExtras().get("job_data");

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getString(R.string.job_signature_title, jobData.getId()));
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDate.setText(formatter.format(new Date()));
        edtName = (EditText) findViewById(R.id.edtName);

        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnDone).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
    }

    private void doSaveSignatureBitmap(){

        if (mSignaturePad.isEmpty()){
            showAlertDialog(this, getString(R.string.error), getString(R.string.job_signature_valid_sign));
            return;
        }

        String name = edtName.getText().toString();
        if (name.equals("")){
            showAlertDialog(this, getString(R.string.error), getString(R.string.job_signature_valid_type_name));
            return;
        }

        Bitmap bmp = mSignaturePad.getSignatureBitmap();
        try {
            FileOutputStream fos = new FileOutputStream(new File(GlobalConstant.getSignatureTempFilePath()));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            progressDialog = showProgressDialog(progressDialog, getString(R.string.please_wait));
            File file = new File(GlobalConstant.getSignatureTempFilePath());
            String imgBase64Str = Utils.encodeTobase64(file);
            JSONObject objParams = new JSONObject();
            objParams.put("description", name);
            objParams.put("image_file_name", "signature.jpg");
            objParams.put("content_type", "image/jpeg");
            objParams.put("image_base_64", imgBase64Str);
            objParams.put("content_length", imgBase64Str.length());
            objParams.put("image_for_id", jobData.getId());
            RestClientUtils.post(this, getString(R.string.PATH_POST_JOB_SIGNATURE), objParams, true, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(getClass().getName(), "Job Add Signature Response:" + response.toString());
                    dismissProgressDialog(progressDialog);

                    DocumentData document = new DocumentData(response);
                    Intent data = new Intent();
                    data.putExtra("document", document);
                    setResult(RESULT_OK, data);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e(getClass().getName(), "Failed1, code:" + statusCode + ", response:" + responseString);
                    if (isActivityActive) {
                        dismissProgressDialog(progressDialog);
                        showErrorAlert(SignatureActivity.this, statusCode);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                    Log.e(getClass().getName(), "Failed2, code:" + statusCode + ", response:" + obj);
                    if (isActivityActive) {
                        dismissProgressDialog(progressDialog);
                        showErrorAlert(SignatureActivity.this, statusCode);
                    }
                }
            });
        }catch(Exception e){}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnDone:
                doSaveSignatureBitmap();
                break;
            case R.id.btnDelete:
                mSignaturePad.clear();
                break;
        }
    }
}
