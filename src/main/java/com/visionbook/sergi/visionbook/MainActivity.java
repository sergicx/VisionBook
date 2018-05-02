package com.visionbook.sergi.visionbook;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.visionbook.sergi.visionbook.helper.SQLite;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] itemsMenu;
    private ListView mDrawerList;
    private SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Demano permis de la camara al usuari nomes obrir l'aplicacio per primer cop
        requestCameraPermission();

        //Obtinc els items del menu del array d'strings
        itemsMenu = getResources().getStringArray(R.array.itemsMenu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, itemsMenu));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            //Crida cuan es tanca el menu
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            //Crida quan s'obre el menu
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Implemento el boto per obrir i tancar el menu
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Creo una instancia de sqllite per a gestionar la llista de llibres
        db = SQLite.getInstancia(this);
        selectItem(0);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Comprobo si es una opcio valida del menu i despres selecciono la opcio
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_capturar:
                selectItem(1);
                return true;

            default:
                //Accio no reconeguda
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Afegeixo el 'layout' de tipus menu per a que mostri el boto de la camera
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
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
                break;
            case 1:
                fragment = new CapturarFragment();
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        //Selecciono el item del menu, canvio el titol del toolbar i tanco el menu
        mDrawerList.setItemChecked(position, true);
        setTitle(itemsMenu[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void requestCameraPermission() {
        //Demanar permisos de la camera en temps d'execucio
        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        //Event que s'executa al clicar a un item del menu
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void onResume() {
        super.onResume();
        //Torno a seleccionar la primera opcio del menu si l'usuari torna a aquesta activitat
        selectItem(0);
    }



}
