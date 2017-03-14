package com.example.user.kupurchase1;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.kupurchase1.Register.RegisterUserID;
import com.example.user.kupurchase1.ServerUtils.GetUserCallback;
import com.example.user.kupurchase1.ServerUtils.ProfileServerRequests;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAGLOG = "로그: LoginActivity: ";


    private EditText etUserID, etUserPassword;
    public TextInputLayout asdUserID, asdUserPassword;
    public Button bLogin;
    public TextView tvRegisterLink, tvFindPassword;

    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserID = (EditText) findViewById(R.id.etUserID);
        etUserPassword = (EditText) findViewById(R.id.etUserPassword);
        asdUserID = (TextInputLayout) findViewById(R.id.asdUserID);
        asdUserPassword = (TextInputLayout) findViewById(R.id.asdUserPassword);

        bLogin = (Button) findViewById(R.id.btLogin);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        tvFindPassword = (TextView) findViewById(R.id.tvFindPassword);

        userLocalStore = new UserLocalStore(this);

        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
        tvFindPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btLogin: {
                String userID = etUserID.getText().toString().trim();
                String userPW = etUserPassword.getText().toString().trim();

                User user = new User(userID, userPW);
                authenticate(user);
                break;
            }

            case R.id.tvRegisterLink:
                Intent intent = new Intent(this, RegisterUserID.class);
                startActivity(intent);
                break;

            case R.id.tvFindPassword:
                break;
        }
    }

    private void authenticate(User user) {
        ProfileServerRequests profileServerRequests = new ProfileServerRequests(this);
        profileServerRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("아이디와 비밀번호가 일치하지 않습니다.");
        dialogBuilder.setPositiveButton("확인", null);
        dialogBuilder.show();
    }

    private void logUserIn( User returnedUser ) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
