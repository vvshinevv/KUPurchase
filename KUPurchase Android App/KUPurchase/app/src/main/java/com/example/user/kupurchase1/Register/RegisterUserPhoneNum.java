package com.example.user.kupurchase1.Register;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.kupurchase1.R;

public class RegisterUserPhoneNum extends AppCompatActivity implements View.OnClickListener{

    public TextView tvBackRegisterUsername, tvGoRegisterUserMailAddress;
    private EditText etUserPhoneNum;
    private TextInputLayout asdUserPhoneNum;

    private String tmp_UserID, tmp_UserPW, tmp_Username, tmp_userPhoneNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_phone_num);

        tvBackRegisterUsername = (TextView) findViewById(R.id.tvBackRegisterUsername);
        tvGoRegisterUserMailAddress = (TextView) findViewById(R.id.tvGoRegisterUserMailAddress);

        etUserPhoneNum = (EditText) findViewById(R.id.etUserPhoneNum);
        asdUserPhoneNum = (TextInputLayout) findViewById(R.id.asdUserPhoneNum);

        etUserPhoneNum.addTextChangedListener(new MyTextWatcher(etUserPhoneNum));
        etUserPhoneNum.setInputType(InputType.TYPE_CLASS_PHONE);
        etUserPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etUserPhoneNum.setText(fetchTelephoneNum(this));

        tvBackRegisterUsername.setOnClickListener(this);
        tvGoRegisterUserMailAddress.setOnClickListener(this);

        Intent intent = getIntent();
        tmp_UserID = intent.getStringExtra("userID");
        tmp_UserPW = intent.getStringExtra("userPW");
        tmp_Username = intent.getStringExtra("username");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBackRegisterUsername:
                Intent intent = new Intent(this, RegisterUsername.class);
                startActivity(intent);
                finish();
                break;

            case R.id.tvGoRegisterUserMailAddress:
                submit();
                break;
        }
    }

    private void submit() {
        if(!validateUserPhoneNum()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterUserPhoneNum.this);
            dialogBuilder.setMessage("휴대폰 번호가 일치하지 않습니다.");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            Intent intent = new Intent(this, RegisterUserMailAddress.class);
            intent.putExtra("userID", tmp_UserID);
            intent.putExtra("userPW", tmp_UserPW);
            intent.putExtra("username", tmp_Username);
            intent.putExtra("userPhoneNum", tmp_userPhoneNum);
            startActivity(intent);
            finish();
        }
    }

    private String fetchTelephoneNum(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
        String tempPhoneNum = telephonyManager.getLine1Number();
        tmp_userPhoneNum = "010-" + tempPhoneNum.substring(5,9) + "-" + tempPhoneNum.substring(9);
        return tmp_userPhoneNum;
    }

    private boolean validateUserPhoneNum() {
        String phone = etUserPhoneNum.getText().toString().trim();
        if(phone.isEmpty() || !isValidPhone(phone)) {
            asdUserPhoneNum.setError(getString(R.string.err_msg_userPhoneNum));
            requestFocus(etUserPhoneNum);
            return false;
        } else {
            asdUserPhoneNum.setErrorEnabled(false);
            tmp_userPhoneNum = etUserPhoneNum.getText().toString().trim();
            return true;
        }
    }

    private boolean isValidPhone(String phone) {
        if(phone.equals(fetchTelephoneNum(this))) {
            return true;
        } else {
            return false;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {

                case R.id.etUserPhoneNum:
                    validateUserPhoneNum();
                    break;
            }
        }
    }
}
