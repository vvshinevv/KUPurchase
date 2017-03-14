package com.example.user.kupurchase1.Register;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.kupurchase1.R;

public class RegisterUserPassword extends AppCompatActivity implements View.OnClickListener{

    public TextView tvBackRegisterUserID, tvGoRegisterUsername;
    private EditText etUserPassword, etUserConfirmPassword;
    private TextInputLayout asdUserPassword, asdUserConfirmPassword;

    public String tmp_UserID, tmp_UserPW = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_password);

        tvBackRegisterUserID = (TextView) findViewById(R.id.tvBackRegisterUserID);
        tvGoRegisterUsername = (TextView) findViewById(R.id.tvGoRegisterUsername);

        etUserPassword = (EditText) findViewById(R.id.etUserPassword);
        etUserConfirmPassword = (EditText) findViewById(R.id.etUserConfirmPassword);

        asdUserPassword = (TextInputLayout) findViewById(R.id.asdUserPassword);
        asdUserConfirmPassword = (TextInputLayout) findViewById(R.id.asdUserConfirmPassword);

        etUserPassword.addTextChangedListener(new MyTextWatcher(etUserPassword));
        etUserConfirmPassword.addTextChangedListener(new MyTextWatcher(etUserConfirmPassword));
        tvBackRegisterUserID.setOnClickListener(this);
        tvGoRegisterUsername.setOnClickListener(this);

        Intent intent = getIntent();
        tmp_UserID = intent.getStringExtra("userID");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBackRegisterUserID:
                Intent intent = new Intent(this, RegisterUserID.class);
                startActivity(intent);
                finish();
                break;

            case R.id.tvGoRegisterUsername:
                submit();
                break;
        }
    }

    private void submit() {
        if(!validateUserConfirmPassword()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterUserPassword.this);
            dialogBuilder.setMessage("비밀번호가 일치하지 않습니다.");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            Intent intent = new Intent(this, RegisterUsername.class);
            intent.putExtra("userID", tmp_UserID);
            intent.putExtra("userPW", tmp_UserPW);
            startActivity(intent);
            finish();
        }
    }

    private boolean validateUserPassword() {
        if(etUserPassword.getText().toString().trim().isEmpty()) {
            asdUserPassword.setError(getString(R.string.err_msg_userPassword));
            requestFocus(etUserPassword);
            return false;
        } else {
            asdUserPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserConfirmPassword() {
        if(etUserConfirmPassword.getText().toString().trim().isEmpty()) {
            asdUserConfirmPassword.setError(getString(R.string.err_msg_userConfirmPassword));
            requestFocus(etUserConfirmPassword);
            return false;
        } else if(!etUserPassword.getText().toString().trim().equals(etUserConfirmPassword.getText().toString().trim())) {
            asdUserConfirmPassword.setError(getString(R.string.err_msg_userConfirmPassword));
            requestFocus(etUserConfirmPassword);
            return false;
        } else {
            asdUserConfirmPassword.setErrorEnabled(false);
            tmp_UserPW = etUserConfirmPassword.getText().toString().trim();
            return true;
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
                case R.id.etUserPassword:
                    validateUserPassword();
                    break;

                case R.id.etUserConfirmPassword:
                    validateUserConfirmPassword();
                    break;
            }
        }
    }
}
