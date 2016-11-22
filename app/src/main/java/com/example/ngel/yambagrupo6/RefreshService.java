package com.example.ngel.yambagrupo6;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
    static final int DELAY =40000; //el tiempo de espera para volver a recopilar datos de twitter
    private boolean runFlag = false; //para saber si el servicio es
    SharedPreferences prefs;
    Twitter twitter;
    DbHelper dbHelper; //clase para conectar la BBDD
    SQLiteDatabase db;

    public RefreshService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");

        dbHelper = new DbHelper(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.runFlag= true;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //El valor de ambos se dejará por defecto en ConfigurationBuilder para no tener que introducirlo ahí manualmente
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
                    //ahora no es necesario abrir la bbdd ya que se abre en cada método de la clase StatusProvider
                    ContentValues values = new ContentValues(); //estructura de datos nombre-valor que mapea los nombres de la BBDD con sus valores

                    for (Status status : timeline) {
                        //Insertar en la BBDD
                        values.clear(); //en cada iteración se limpia la estructura de nombres-valor
                        //MAPEO DE CADA ELEMENTO DE LA TABLA
                        values.put(StatusContract.Column.ID, status.getId());
                        values.put(StatusContract.Column.USER, status.getUser().getName());
                        values.put(StatusContract.Column.MESSAGE, status.getText());
                        values.put(StatusContract.Column.CREATED_AT, status.getCreatedAt().getTime());
                        /*INSERCCIÓN DEL CONTENTVALUES EN LA BBDD SIN USAR CONTENT PROVIDER
                            -WithOnConflict =>solo actualizaciones de estado nuevas
                            db.insertWithOnConflict (StatusContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);*/

                        //INSERCCIÓN DEL CONTENTVALUES EN LA BBDD CON CONTENT PROVIDER
                        Uri uri = getContentResolver().insert(StatusContract.CONTENT_URI, values); //Método insert del StatusProvider

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
