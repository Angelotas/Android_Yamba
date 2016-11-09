package com.example.ngel.yambagrupo6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    //ESTA SERÁ LA ACTIVITY VACÍA QUE SE MOSTRARÁ AL INICIAR LA APP Y DESDE LA QUE SE ACCEDE AL MENÚ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*Evento cuando se pulse al botón de menú*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); //infla el xml del menú dentro del activity
        return true;
    }

    /*Captura los eventos recogidos al pulsar sobre un item del menú*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class)); //relaciona con el activity de preferencias y su
                return true;                                             //correspondiente fragment
            case R.id.action_tweet:
                startActivity(new Intent(this, StatusActivity.class));   //relacinoa con el activity de Status (tweet) y su
                return true;                                             //correspondiente fragment
            default:
                return false;
        }
    }
}
