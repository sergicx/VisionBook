package com.visionbook.sergi.visionbook;


import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class CapturarFragment extends Fragment {

    private CameraSource mCameraSource;
    private SurfaceView mCameraView;
    private View view;
    private TextRecognizer textRecognizer;
    private final String LOG = "LOG";

    public CapturarFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startCameraSource() {
        mCameraView = null;
        mCameraSource = null;

        mCameraView = (SurfaceView) view.findViewById(R.id.surfaceView);

        //Creo el reconeixedor de textos
        textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w("Error", "Les dependencies del detector de text no esta carregat encara");
        } else {

            //Inicialitzo la camara amb els seguents parametres
            mCameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(800, 800)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            //Si es té permisos a la camara s'inicialitzará i se li pasará al SurfaceView (mCameraView) per a que es mostri
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Obligo al usuari a fer clic a la pantalla per a començar amb el reconeixement de text
            //ja que a vegades la camara no está enfoocada
            mCameraView.setOnClickListener((View v) -> {
                Toast.makeText(getContext(), "Buscant llibre, no moguis el dispositiu...", Toast.LENGTH_LONG).show();

                //Inicio el detector de textos
                textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {


                    @Override
                    public void release() {
                    }


                    //El metode de continuacio s'executará cada cop que detecti un text.
                    //Primer comprobo si la quantitat de lletres processades es major de 0 i aleshores
                    //inicio un fil per a concatenar totes les lletres trobades i faig una cerca del text
                    //detectat a la api de llibres de google
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void receiveDetections(Detector.Detections<TextBlock> detections) {
                        final SparseArray<TextBlock> items = detections.getDetectedItems();
                        textRecognizer.release();
                        if (items.size() != 0) {
                            getActivity().runOnUiThread(() -> {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                }

                                //Evito els salts de linia i concateno els espais amb el caracter + per a que sigui mes familiar amb la URL
                                String capturaFinal = stringBuilder.toString().replace("\n", "+").replace(" ", "+");

                                try {
                                    //Obtinc una instancia de llibre per a poder comprobar si s'ha trobat contingut
                                    Llibre llibre = new obtenirDadesLlibre().execute(capturaFinal).get();
                                    if (llibre == null){
                                        //Si no l'ha trobat, torno a buscar el llibre pero retallant el text capturat fins a la seva meitat
                                        llibre = new obtenirDadesLlibre().execute(capturaFinal.substring(0, capturaFinal.length()/2)).get();
                                        if (llibre == null){
                                            //Si la casuistica anterior tampoc troba el llibre, retallo el llibre desde la meitat fins al final
                                            new obtenirDadesLlibre().execute(capturaFinal.substring(capturaFinal.length()/2, capturaFinal.length()));
                                            //Si no troba el llibre mostro un Toast informant de que no l'ha trobat
                                            Toast.makeText(getContext(), "Llibre no trobat", Toast.LENGTH_LONG).show();
                                            //Torno a executar el fragment
                                            getFragmentManager().beginTransaction().detach(CapturarFragment.this).attach(CapturarFragment.this).commit();
                                        }
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                            });
                        }

                    }
                });

            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onResume() {
        super.onResume();
        startCameraSource();
    }

    private class obtenirDadesLlibre extends AsyncTask<String, Void, Llibre>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Començant la cerca...");
        }

        @Override
        protected Llibre doInBackground(String... strings) {
            System.out.println("BUSCANT PER: "+ strings[0]);
            HttpHandler sh = new HttpHandler();
            //Concateno la url de la consulta a la API amb el text que hagi capturat
            String url = "https://www.googleapis.com/books/v1/volumes?q="+strings[0];
            Log.e(LOG, url);
            //Faig una petició GET utilitzant la classe HttpHandler
            String jsonStr = sh.makeServiceCall(url);

            if(jsonStr != null){
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    System.out.println("trobats: "+jsonObj.getInt("totalItems"));
                    if (jsonObj.getInt("totalItems") > 0){
                        JSONArray llibres = jsonObj.getJSONArray("items");
                        JSONObject primerLlibre = llibres.getJSONObject(0);
                        JSONObject volumeInfo = primerLlibre.getJSONObject("volumeInfo");
                        String titol = volumeInfo.getString("title");
                        String descripcio = "Sense descripció";
                        if (volumeInfo.has("description"))
                            descripcio = volumeInfo.getString("description");

                        ArrayList<String> llistaAutors = new ArrayList<>();
                        JSONArray autorsJSON = volumeInfo.getJSONArray("authors");


                        for (int i=0; i< autorsJSON.length(); i++){
                            llistaAutors.add(autorsJSON.getString(i));
                        }


                        System.out.println("Titol: "+ titol);
                        Llibre llibre = new Llibre();
                        llibre.setTitol(titol);
                        llibre.setDescripcio(descripcio);

                        llibre.setAutors(llistaAutors);
                        return llibre;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Llibre result) {
            super.onPostExecute(result);

            if (result != null) {
                //Si ha trobat el llibre a la api, obrirá l'activitat detall pasantli el objecte llibre
                Intent iResultat = new Intent(getActivity(), LlibreDetall.class);
                iResultat.putExtra("resultat", result);
                startActivity(iResultat);
            }else {
                //Contingut else borrat temporalment

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_capturar, container, false);
        startCameraSource();

        return view;
    }


}