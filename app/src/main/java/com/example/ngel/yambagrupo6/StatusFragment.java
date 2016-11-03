package com.example.ngel.yambagrupo6;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class StatusFragment extends Fragment implements View.OnClickListener, TextWatcher{

    private static final String TAG = "StatusActivity"; //variable para los LOG
    EditText editStatus;
    Button buttonTweet;
    Twitter twitter;
    TextView textCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container, Bundle savedInstanceStat) {

        View view = inflater.inflate(R.layout.fragment_status,container, false);


        // Find views
        editStatus = (EditText) view.findViewById(R.id.editStatus);

        buttonTweet = (Button) view.findViewById(R.id.buttonTweet);
        buttonTweet.setOnClickListener(this);

        textCount = (TextView) view.findViewById(R.id.textCount);
        textCount.setText(Integer.toString(140));
        textCount.setTextColor(Color.GREEN);
        editStatus.addTextChangedListener(this);

        //Aqui la configuración Twiter
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey("H8jPiRqgcIzUjyiLxLMeIpD9U")
                .setOAuthConsumerSecret("wAMU8qfrRZDMmRztWpnLPIJNpSxkw8inTjHN49riohOoYK1EjA")
                .setOAuthAccessToken("781219984072663041-BSJyn92gQ4IIW0w2xhzRsJ9zzVSWt4F")
                .setOAuthAccessTokenSecret("cP56dBKhAYmLdcJ2RmWvf7yJFGhinCgTdilHMP9mhhQHL");
        TwitterFactory factory =new TwitterFactory(builder.build());
        twitter = factory.getInstance(); //conexión al servicio online que soporta la API de Twitter

        return view;
    }

    @Override
    public void onClick(View v) {
        String status = editStatus.getText().toString();
        Log.d(TAG, "onClicked");
        new PostTask().execute(status); //se ejecutará en segundo plano

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable statusText) {
        int count = 140 - statusText.length();
        textCount.setText(Integer.toString(count));
        textCount.setTextColor(Color.GREEN);
        if (count < 10)
            textCount.setTextColor(Color.YELLOW);
        if (count < 0)
            textCount.setTextColor(Color.RED);
    }

    //SUBCLASE PARA PUBLICAR EN TWITTER EN SEGUNDO PLANO
    private final class PostTask extends AsyncTask<String, Void, String> {
        // Llamada al empezar
        ProgressDialog progress;

        @Override
        protected void onPreExecute(){ //previo a la ejecución
            progress = ProgressDialog.show(StatusFragment.this.getActivity(),getString(R.string.title_progresDialog) , getString(R.string.content_progresDialog),true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                twitter.updateStatus(params[0]);
                return getString(R.string.envio_correcto);
            } catch (TwitterException e) {
                Log.e(TAG, "Fallo en el envío");
                e.printStackTrace();
                return getString(R.string.envio_incorrecto);
            }
        }
        // Llamada cuando la actividad en background ha terminad
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.dismiss(); //finaliza el progressDialog
            Toast.makeText(StatusFragment.this.getActivity(),result,Toast.LENGTH_LONG).show();
        }
    }
}
