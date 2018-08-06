package com.gametimegiving.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 777;
    final public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = getClass().getSimpleName();
    private final String gameId = "suYroi6ZuratHkBDuyF7";
    String userId;
    private String name;
    private String email;
    private Uri photoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Utilities.GenerateHashKey(this);
        // setContentView(R.layout.activity_login);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userId = user.getUid();
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();
            Bundle bundle = new Bundle();
            bundle.putString("user", userId);
            bundle.putString("username", name);
            bundle.putString("game", gameId);
            if (photoUrl != null) {
                bundle.putString("photoUrl", photoUrl.toString());
            } else {
                bundle.putString("photoUrl", "");
            }
            Log.d(TAG, String.format("userid is %s and game is %s inside the oncreate of login activity", userId, gameId));
            Intent intent = new Intent(this, GameBoardActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.GTGAppTheme)
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                                    new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
            //            startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .setAvailableProviders(Arrays.asList(
//                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
//                                    new AuthUI.IdpConfig.FacebookBuilder().build(),
//                                    new AuthUI.IdpConfig.TwitterBuilder().build(),
//                                    new AuthUI.IdpConfig.EmailBuilder().build(),
//                                    new AuthUI.IdpConfig.PhoneBuilder().build()))
//                            .build(),
//                    RC_SIGN_IN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                userId = user.getUid();
                name = user.getDisplayName();
                email = user.getEmail();
                photoUrl = user.getPhotoUrl();
                Bundle bundle = new Bundle();
                bundle.putString("user", userId);
                bundle.putString("username", name);
                bundle.putString("game", gameId);
                if (photoUrl != null) {
                    bundle.putString("photoUrl", photoUrl.toString());
                } else {
                    bundle.putString("photoUrl", "");
                }
                Log.d(TAG, String.format("userid is %s and game is %s inside the create player of login activity", userId, gameId));
                Intent intent = new Intent(this, GameBoardActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
