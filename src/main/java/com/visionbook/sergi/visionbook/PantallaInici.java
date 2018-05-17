package com.visionbook.sergi.visionbook;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.visionbook.sergi.visionbook.helper.Helper;
import com.visionbook.sergi.visionbook.peticions_api.CreaUsuari;

public class PantallaInici extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private FirebaseUser usuari;
    private Animation amuntCapAvall, avallCapAmunt;
    private Button btnLogin;
    private TextView tvTitol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inici);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvTitol = (TextView) findViewById(R.id.tvTitol);

        amuntCapAvall = AnimationUtils.loadAnimation(this, R.anim.amuntcapavall);
        tvTitol.setAnimation(amuntCapAvall);

        avallCapAmunt = AnimationUtils.loadAnimation(this, R.anim.avallcapamunt);
        btnLogin.setAnimation(avallCapAmunt);

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
                new CreaUsuari().execute(usuari.getUid(), usuari.getDisplayName(), usuari.getEmail());
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
