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

import com.example.user.kupurchase1.LoginActivity;
import com.example.user.kupurchase1.R;

public class RegisterUserID extends AppCompatActivity implements View.OnClickListener {

    public TextView tvBackLoginActivity, tvGoRegisterUserPassword;
    private EditText etUserID;
    private TextInputLayout asdUserID;

    public String tmp_UserID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_id);

        tvBackLoginActivity = (TextView) findViewById(R.id.tvBackLoginActivity);
        tvGoRegisterUserPassword = (TextView) findViewById(R.id.tvGoRegisterUserPassword);

        etUserID = (EditText) findViewById(R.id.etUserID);
        asdUserID = (TextInputLayout) findViewById(R.id.asdUserID);

        etUserID.addTextChangedListener(new MyTextWatcher(etUserID));
        tvBackLoginActivity.setOnClickListener(this);
        tvGoRegisterUserPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBackLoginActivity: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            }

            case R.id.tvGoRegisterUserPassword: {
                submit();
                break;
            }
        }
    }

    private boolean validateUserID() {
        if(etUserID.getText().toString().trim().isEmpty()) {
            asdUserID.setError(getString(R.string.err_msg_userID));
            requestFocus(etUserID);
            return false;
        } else {
            asdUserID.setErrorEnabled(false);
            tmp_UserID = etUserID.getText().toString().trim();
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void submit() {
        if(!validateUserID()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterUserID.this);
            dialogBuilder.setMessage("아이디를 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            Intent intent = new Intent(this, RegisterUserPassword.class);
            intent.putExtra("userID", tmp_UserID);
            startActivity(intent);
            finish();
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
                case R.id.etUserID:
                    validateUserID();
                    break;
            }
        }
    }
}
