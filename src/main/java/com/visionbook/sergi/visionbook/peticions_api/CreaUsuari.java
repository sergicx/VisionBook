package com.visionbook.sergi.visionbook.peticions_api;

import android.os.AsyncTask;

import com.visionbook.sergi.visionbook.helper.HttpHandler;

public class CreaUsuari extends AsyncTask<String, Void, Void>{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... strings) {
        HttpHandler sh = new HttpHandler();

        String url = "http://visionbook.ml/creaUsuari.php?id=" + strings[0] + "&nom=" + strings[1] + "&email=" + strings[2];
        //Faig una petició http utilitzant la classe HttpHandler
        sh.makeServiceCall(url);
        return null;
    }

}
