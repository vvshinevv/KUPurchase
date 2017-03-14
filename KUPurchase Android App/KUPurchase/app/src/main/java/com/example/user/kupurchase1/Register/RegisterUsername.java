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

public class RegisterUsername extends AppCompatActivity implements View.OnClickListener{

    public TextView tvBackRegisterUserPassword, tvGoRegisterUserPhoneNum;
    private EditText etUsername;
    private TextInputLayout asdUsername;

    public String tmp_UserID, tmp_UserPW, tmp_Username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_username);

        tvBackRegisterUserPassword = (TextView) findViewById(R.id.tvBackRegisterUserPassword);
        tvGoRegisterUserPhoneNum = (TextView) findViewById(R.id.tvGoRegisterUserPhoneNum);

        etUsername = (EditText) findViewById(R.id.etUsername);
        asdUsername = (TextInputLayout) findViewById(R.id.asdUsername);

        etUsername.addTextChangedListener(new MyTextWatcher(etUsername));
        tvBackRegisterUserPassword.setOnClickListener(this);
        tvGoRegisterUserPhoneNum.setOnClickListener(this);

        Intent intent = getIntent();
        tmp_UserID = intent.getStringExtra("userID");
        tmp_UserPW = intent.getStringExtra("userPW");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBackRegisterUserPassword:
                Intent intent = new Intent(this, RegisterUserPassword.class);
                startActivity(intent);
                finish();
                break;

            case R.id.tvGoRegisterUserPhoneNum:
                submit();
                break;
        }
    }

    private void submit() {
        if(!validateUsername()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterUsername.this);
            dialogBuilder.setMessage("이름을 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            Intent intent = new Intent(this, RegisterUserPhoneNum.class);
            intent.putExtra("userID", tmp_UserID);
            intent.putExtra("userPW", tmp_UserPW);
            intent.putExtra("username", tmp_Username);
            startActivity(intent);
            finish();
        }
    }

    private boolean validateUsername() {
        if(etUsername.getText().toString().trim().isEmpty()) {
            asdUsername.setError(getString(R.string.err_msg_username));
            requestFocus(etUsername);
            return false;
        } else {
            asdUsername.setErrorEnabled(false);
            tmp_Username = etUsername.getText().toString().trim();
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
                case R.id.etUsername:
                    validateUsername();
                    break;
            }
        }
    }
}
