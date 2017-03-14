package com.example.user.kupurchase1.Register;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.kupurchase1.GCMUtils.QuickstartPreferences;
import com.example.user.kupurchase1.GCMUtils.RegistrationIntentService;
import com.example.user.kupurchase1.LoginActivity;
import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.ServerUtils.GetUserCallback;
import com.example.user.kupurchase1.ServerUtils.ProfileServerRequests;
import com.example.user.kupurchase1.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class RegisterUserMailAddress extends AppCompatActivity implements View.OnClickListener{

    private String TAGLOG = "로그: RegisterUserMailAddress: ";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public TextView tvBackRegisterUserPhoneNum, tvRegister, tvRegulation, tvPersonalRegulation;
    private EditText etUserMailAddress;
    private TextInputLayout asdUserMailAddress;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public String userID, userPW, username, userPhoneNum, userMailAddress, userToken = "";

    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 READY일 경우
                    userToken = intent.getStringExtra("token");
                    Log.e(TAGLOG, "Token : " + userToken);
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_mail_address);
        registBroadcastReceiver();

        tvBackRegisterUserPhoneNum = (TextView) findViewById(R.id.tvBackRegisterUserPhoneNum);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvRegulation = (TextView) findViewById(R.id.tvRegulation);
        tvPersonalRegulation = (TextView) findViewById(R.id.tvPersonalRegulation);
        etUserMailAddress = (EditText) findViewById(R.id.etUserMailAddress);
        asdUserMailAddress = (TextInputLayout) findViewById(R.id.asdUserMailAddress);

        etUserMailAddress.addTextChangedListener(new MyTextWatcher(etUserMailAddress));
        tvBackRegisterUserPhoneNum.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvRegulation.setOnClickListener(this);
        tvPersonalRegulation.setOnClickListener(this);

        SpannableString regulation = new SpannableString("이용약관");
        SpannableString personal_regulation = new SpannableString("개인정보취급방침");
        regulation.setSpan(new UnderlineSpan(), 0, regulation.length(), 0);
        personal_regulation.setSpan(new UnderlineSpan(), 0, personal_regulation.length(), 0);
        tvRegulation.setText(regulation);
        tvPersonalRegulation.setText(personal_regulation);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userPW = intent.getStringExtra("userPW");
        username = intent.getStringExtra("username");
        userPhoneNum = intent.getStringExtra("userPhoneNum");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInstanceIdToken();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBackRegisterUserPhoneNum:
                Intent intent = new Intent(this, RegisterUserPhoneNum.class);
                startActivity(intent);
                finish();
                break;

            case R.id.tvRegister:
                User user = new User(userID, userPW, username, userPhoneNum, userMailAddress, userToken);
                register(user);
                break;

            case R.id.tvRegulation:
                showRegulation(this);
                break;

            case R.id.tvPersonalRegulation:
                showPersonalRegulation(this);
                break;
        }
    }

    private void register(User user) {
        if(!validateUserMailAddress()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterUserMailAddress.this);
            dialogBuilder.setMessage("메일 주소를 적어주세요.");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            ProfileServerRequests profileServerRequests = new ProfileServerRequests(this);
            profileServerRequests.storeUserDataInBackground(user, new GetUserCallback() {
                @Override
                public void done(User returnedUser) {
                    startActivity(new Intent(RegisterUserMailAddress.this, LoginActivity.class));
                    finish();
                }
            });
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAGLOG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void getInstanceIdToken() {
        if(checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private static void showRegulation(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.regulation_title)
                .setView(LayoutInflater.from(context).inflate(R.layout.activity_regulation, null))
                .setPositiveButton("확인", null)
                .show();
    }

    private static void showPersonalRegulation(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.personal_regulation_title)
                .setView(LayoutInflater.from(context).inflate(R.layout.activity_personal_regulation, null))
                .setPositiveButton("확인", null)
                .show();
    }

    private boolean validateUserMailAddress() {
        if(etUserMailAddress.getText().toString().isEmpty()) {
            asdUserMailAddress.setError(getString(R.string.err_msg_userMailAddress));
            requestFocus(etUserMailAddress);
            return false;
        } else {
            asdUserMailAddress.setErrorEnabled(false);
            userMailAddress = etUserMailAddress.getText().toString().trim();
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

                case R.id.etUserMailAddress:
                    validateUserMailAddress();
                    break;
            }
        }
    }
}
