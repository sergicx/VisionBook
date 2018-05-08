package com.visionbook.sergi.visionbook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.principal_menu, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_llistallibres) {
            selectItem(0);
        } else if (id == R.id.nav_capturarllibre) {
            selectItem(1);
        } else if (id == R.id.nav_about) {
            selectItem(2);
        }else if (id == R.id.nav_logout){
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectItem(int position) {
        //Executo el fragment depenent de l'opcio seleccionada per l'usuari
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();

        switch(position) {
            default:
            case 0:
                fragment = new LlibresFragment();
                setTitle(getResources().getString(R.string.llibres_capturats));
                navigationView.setCheckedItem(R.id.nav_llistallibres);
                break;
            case 1:
                fragment = new CapturarFragment();
                setTitle(getResources().getString(R.string.capturar_llibre));
                navigationView.setCheckedItem(R.id.nav_capturarllibre);
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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
