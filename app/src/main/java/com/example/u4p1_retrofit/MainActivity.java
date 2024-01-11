package com.example.u4p1_retrofit;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInClient.launch(GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                        getString(R.string.default_web_client_id)).build()).getSignInIntent());
    }

    ActivityResultLauncher<Intent> signInClient =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                try {
                    FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(
                            GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult(ApiException.class)
                                    .getIdToken(), null));
                } catch (ApiException e) {
                }
            });
}