package com.visionbook.sergi.visionbook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.visionbook.sergi.visionbook.capturar_llibre.CapturarFragment;
import com.visionbook.sergi.visionbook.helper.Helper;
import com.visionbook.sergi.visionbook.llista_llibres.LlibresFragment;

public class Principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CAMERA = 0;

    private NavigationView navigationView;
    private FirebaseUser usuari;
    private TextView tvNomUsuari, tvCorreu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /*
           Codi generat per Android Studio per a gestionar el menu
           =======================================================
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        /*
           =======================================================
         */

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        tvNomUsuari = (TextView) headerView.findViewById(R.id.tvNomUsuari);
        tvCorreu = (TextView) headerView.findViewById(R.id.tvCorreu);
        usuari = FirebaseAuth.getInstance().getCurrentUser();
        tvNomUsuari.setText(usuari.getDisplayName());
        tvCorreu.setText(usuari.getEmail());
        requestCameraPermission();
        navigationView.setCheckedItem(R.id.nav_llistallibres);

    }



    @Override
    public void onBackPressed() {
        //Metode implementat per a que es tanqui el menú quan l'usuari li doni cap endarrere al movil
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_capturar) {
            navigationView.setCheckedItem(R.id.nav_capturarllibre);
            selectItem(1);
            return true;
        }

        return false;

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //aqui es gestiona les accions de cada item del menú
        int id = item.getItemId();

        if (id == R.id.nav_llistallibres) {
            selectItem(0);
        } else if (id == R.id.nav_capturarllibre) {
            selectItem(1);
        } else if (id == R.id.nav_about) {
            selectItem(2);
        }else if (id == R.id.nav_logout){
            DialogInterface.OnClickListener tancarSesioListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        logout();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };
            Helper.confirmacioTancarSesio(this, tancarSesioListener, getResources().getString(R.string.confirmacio_logout));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectItem(int position) {
        //Executo el fragment depenent de l'opcio seleccionada per l'usuari
        Fragment fragment;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch(position) {
            default:
            case 0:
                fragment = new LlibresFragment();
                setTitle(getResources().getString(R.string.llibres_capturats));
                navigationView.setCheckedItem(R.id.nav_llistallibres);
                break;
            case 1:
                if (Helper.hiHaInternet(this)) {
                    fragment = new CapturarFragment();
                    setTitle(getResources().getString(R.string.capturar_llibre));
                    navigationView.setCheckedItem(R.id.nav_capturarllibre);
                }else {
                    Snackbar.make(getWindow().getDecorView(), getResources().getString(R.string.error_internet), Snackbar.LENGTH_SHORT).show();
                    fragment = new LlibresFragment();
                }
                break;
            case 2:
                fragment = new SobreLaAppFragment();
                setTitle(getResources().getString(R.string.sobre_app));
                navigationView.setCheckedItem(R.id.nav_about);
                break;
        }

        //Es substitueix el fragment anterior per el nou que l'usuari hagi seleccionat
        ((FrameLayout) findViewById(R.id.content_frame)).removeAllViews();

        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void requestCameraPermission() {
        //Demanar permisos de la camera en temps d'execucio
        final String[] permissions = new String[]{android.Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA);
        }
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intentInici = new Intent(this, PantallaInici.class);
        startActivity(intentInici);
        finish();
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public void onRestart() {
        super.onRestart();
        //Torno a seleccionar la primera opcio del menu si l'usuari torna a aquesta activitat
        selectItem(0);
    }
}
