package com.myapp.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myapp.android.R;
import com.myapp.android.preferences.AppPreference;

public class CreatePinActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtEnterPin, edtReenterPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);

        edtEnterPin = (EditText) findViewById(R.id.edtEnterPin);
        edtEnterPin.setTypeface(Typeface.DEFAULT);
        edtReenterPin = (EditText) findViewById(R.id.edtReenterPin);
        edtReenterPin.setTypeface(Typeface.DEFAULT);

        findViewById(R.id.btnSave).setOnClickListener(this);
    }

    private void doSavePin(){
        String pin = edtEnterPin.getText().toString();
        if (pin.equals("") || pin.length() < 4){
            showAlertDialog(this, getString(R.string.error), getString(R.string.create_pin_enter_four_digit_pin));
            edtEnterPin.requestFocus();
            return;
        }

        String repin = edtReenterPin.getText().toString();
        if (!repin.equals(pin)){
            showAlertDialog(this, getString(R.string.error), getString(R.string.create_pin_reenter_pin_wrong));
            edtReenterPin.requestFocus();
            return;
        }

        AppPreference.setPin(this, pin);
        enterApp();
    }

    private void enterApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                doSavePin();
                break;
        }
    }
}
