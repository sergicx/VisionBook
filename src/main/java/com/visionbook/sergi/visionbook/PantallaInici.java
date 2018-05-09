package com.visionbook.sergi.visionbook;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.visionbook.sergi.visionbook.helper.Helper;

public class PantallaInici extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private FirebaseUser usuari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inici);
        usuari = FirebaseAuth.getInstance().getCurrentUser();

        if (usuari != null){
            Intent principal = new Intent(this, Principal.class);
            startActivity(principal);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                usuari = FirebaseAuth.getInstance().getCurrentUser();
                Intent principal = new Intent(this, Principal.class);
                startActivity(principal);
                finish();
            } else {

            }
        }
    }

    public void iniciarSesio(View v) {
        if (Helper.hiHaInternet(this)) {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .build(),
                        RC_SIGN_IN);
            }
        }else {
            Snackbar.make(getWindow().getDecorView().getRootView(), getResources().getString(R.string.error_internet), Snackbar.LENGTH_SHORT).show();
        }
    }
}
