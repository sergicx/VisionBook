package com.visionbook.sergi.visionbook.peticions_api;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.visionbook.sergi.visionbook.R;
import com.visionbook.sergi.visionbook.helper.HttpHandler;
import com.visionbook.sergi.visionbook.mestre_llibre.LlibreDetall;

import org.json.JSONException;
import org.json.JSONObject;

public class NumeroComentaris extends AsyncTask<String, Void, Integer>{
    private Context context;
    private ProgressDialog dialog;

    public NumeroComentaris(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
            dialog = ProgressDialog.show(context, context.getResources().getString(R.string.carregant1), context.getResources().getString(R.string.carregant2), true);
            dialog.show();
    }

    @Override
    protected Integer doInBackground(String... strings) {
        HttpHandler sh = new HttpHandler();
        String url = "http://visionbook.ml/getComentaris.php?id_llibre=" + strings[0];
        String jsonStr = sh.makeServiceCall(url);
        int numComentaris = 0;

        if (jsonStr != null){
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                numComentaris = jsonObj.getInt("trobats");
                System.out.println("id: " + strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return numComentaris;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        Button btnComentaris = (Button) ((LlibreDetall) context).findViewById(R.id.btnObrirComentaris);
        btnComentaris.setText(context.getResources().getString(R.string.boto_comentaris, result));
        dialog.dismiss();
    }
}
