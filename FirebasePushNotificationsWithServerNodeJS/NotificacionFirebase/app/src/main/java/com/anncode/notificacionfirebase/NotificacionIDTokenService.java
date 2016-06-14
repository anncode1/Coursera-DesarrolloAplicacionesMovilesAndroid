package com.anncode.notificacionfirebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by anahisalgado on 08/06/16.
 */
public class NotificacionIDTokenService extends FirebaseInstanceIdService {

    private static final String TAG = "FIREBASE_TOKEN";
    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        Log.d(TAG, "Solicitando Token");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, token);

    }


}
