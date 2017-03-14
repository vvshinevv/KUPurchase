package com.example.user.kupurchase1.GCMUtils;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by user on 2015-12-29.
 */
public class MyInstanceIDListener extends InstanceIDListenerService {
    private static final String TAG = "MyInstanceIDLS";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
