package com.gametimegiving.android.Helpers;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class GTGFirebaseMessageService extends FirebaseMessagingService {
    private static final String TAG = "GTGFirebaseMessageServ";
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String notificationBody = "";
        String notificationTitle = "";
        String notificationData = "";

        try {
            notificationData = remoteMessage.getData().toString();
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        } catch (NullPointerException ex) {

            Log.e(TAG, "onMessageReceived Error: " + ex.toString());
        }

        Log.d(TAG, "onMessageReceived: data: " + notificationData);
        Log.d(TAG, "onMessageReceived: notification body: " + notificationBody);
        Log.d(TAG, "onMessageReceived: notification title: " + notificationTitle);

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();

    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.d(TAG, "onNewToken: token: " + token);
        sendRegistrationToSever(token);
    }

    private void sendRegistrationToSever(String token) {
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "sendRegistrationToSever: " + token);
        String userid = fbuser.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("userid", userid);
        user.put("messagetoken", token);
        db.collection("users").document(userid).update(user);

    }
}
