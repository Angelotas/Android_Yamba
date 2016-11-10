package com.example.ngel.yambagrupo6;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class RefreshService extends IntentService {

    static final String TAG = "RefreshService";
    static final int DELAY = 5000; //el tiempo de espera para volver a recopilar datos de twitter
    private boolean runFlag = false; //para saber si el servicio es
    SharedPreferences prefs;
    Twitter twitter;

    public RefreshService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.runFlag= true;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String accesstoken = prefs.getString("accesstoken", "");
        String accesstokensecret = prefs.getString("accesstokensecret", "");

        Log.d(TAG, "onStarted");

        while (runFlag) {
            Log.d(TAG, "Updater running");
            try {

                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey("H8jPiRqgcIzUjyiLxLMeIpD9U")
                        .setOAuthConsumerSecret("wAMU8qfrRZDMmRztWpnLPIJNpSxkw8inTjHN49riohOoYK1EjA")
                        .setOAuthAccessToken("781219984072663041-BSJyn92gQ4IIW0w2xhzRsJ9zzVSWt4F")
                        .setOAuthAccessTokenSecret("cP56dBKhAYmLdcJ2RmWvf7yJFGhinCgTdilHMP9mhhQHL");
                TwitterFactory factory =new TwitterFactory(builder.build());
                twitter = factory.getInstance(); //conexión al servicio online que soporta la API de Twitter

                try {
                    List<Status> timeline = twitter.getHomeTimeline();
                    // Imprimimos las actualizaciones en el log
                    for (Status status : timeline) {
                        Log.d(TAG, String.format("%s: %s", status.getUser().getName(),
                                status.getText()));
                    }
                } catch (TwitterException e) {
                    Log.e(TAG, "Failed to fetch the timeline", e);
                }

                Log.d(TAG, "Updater ran");
                Thread.sleep(DELAY);

            } catch (InterruptedException e) {
                runFlag = false;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy(); //finaliza la ejecución del servicio
        this.runFlag = false;
        Log.d(TAG, "onDestroyed");
    }



}
