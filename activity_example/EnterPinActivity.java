package com.myapp.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myapp.android.R;
import com.myapp.android.preferences.AppPreference;

public class EnterPinActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtEnterPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        edtEnterPin = (EditText) findViewById(R.id.edtEnterPin);
        edtEnterPin.setTypeface(Typeface.DEFAULT);
        edtEnterPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pin = edtEnterPin.getText().toString();
                if (pin.equals(AppPreference.getPin(EnterPinActivity.this))){
                    enterApp();
                }
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnResetPin).setOnClickListener(this);
    }

    private void doLogin(){
        String pin = edtEnterPin.getText().toString();
        if (pin.equals("") || pin.length() < 4){
            showAlertDialog(this, getString(R.string.error), getString(R.string.enter_pin_valid_no_pin));
            edtEnterPin.requestFocus();
            return;
        }

        if (!pin.equals(AppPreference.getPin(this))){
            showAlertDialog(this, getString(R.string.error), getString(R.string.enter_pin_not_match));
            edtEnterPin.requestFocus();
            return;
        }

        enterApp();
    }

    private void enterApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToResetPin(){
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("has_anim", false);
        startActivity(intent);
        overridePendingTransition(R.anim.appear_from_right, R.anim.disappear_to_left);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                doLogin();
                break;
            case R.id.btnResetPin:
                goToResetPin();
                break;
        }
    }
}
