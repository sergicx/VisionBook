package com.visionbook.sergi.visionbook;


import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.visionbook.sergi.visionbook.entitats.Llibre;

import java.io.IOException;
import java.text.Normalizer;
import java.util.concurrent.ExecutionException;


public class CapturarFragment extends Fragment {

    private CameraSource mCameraSource;
    private SurfaceView mCameraView;
    private View view;
    private TextRecognizer textRecognizer;


    public CapturarFragment() {

    }

    private void startCameraSource() {
        mCameraView = null;
        mCameraSource = null;

        mCameraView = (SurfaceView) view.findViewById(R.id.surfaceView);

        //Preparo el reconeixedor de textos
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
                Toast.makeText(getActivity(), getResources().getString(R.string.buscant_llibre), Toast.LENGTH_LONG).show();

                //Inicio el detector de textos
                textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {


                    @Override
                    public void release() {
                    }


                    //El metode de continuacio s'executará cada cop que detecti un text.
                    //Primer comprobo si la quantitat de lletres processades es major de 0 i aleshores
                    //inicio un fil per a concatenar totes les lletres trobades i faig una cerca del text
                    //detectat a la api de llibres de google
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
                                capturaFinal = treureAccents(capturaFinal);

                                try {
                                    //Obtinc una instancia de llibre per a poder comprobar si s'ha trobat contingut
                                    Llibre llibre = new ObtenirDadesLlibre(getActivity()).execute(capturaFinal).get();
                                    if (llibre == null){
                                        //Si no l'ha trobat, torno a buscar el llibre pero retallant el text capturat fins a la seva meitat
                                        llibre = new ObtenirDadesLlibre(getActivity()).execute(capturaFinal.substring(0, capturaFinal.length()/2)).get();
                                        if (llibre == null){
                                            //Si la casuistica anterior tampoc troba el llibre, retallo el llibre desde la meitat fins al final
                                            llibre = new ObtenirDadesLlibre(getActivity()).execute(capturaFinal.substring(capturaFinal.length()/2, capturaFinal.length())).get();
                                            if (llibre == null){
                                                //Si no troba el llibre mostro un Toast informant de que no l'ha trobat
                                                Toast.makeText(getActivity(), getResources().getString(R.string.llibre_no_trobat), Toast.LENGTH_LONG).show();
                                                //Torno a executar el fragment
                                                getFragmentManager().beginTransaction().detach(CapturarFragment.this).attach(CapturarFragment.this).commit();
                                            }
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


    public void onResume() {
        super.onResume();
        startCameraSource();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_capturar, container, false);
        startCameraSource();

        return view;
    }

    private String treureAccents(String url){
        String normalize = Normalizer.normalize(url, Normalizer.Form.NFD);
        String senseAccents = normalize.replaceAll("[^\\p{ASCII}]", "");
        return senseAccents;
    }


}