package com.example.ngel.yambagrupo6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ángel on 16/11/2016.
 */

public class DbHelper extends SQLiteOpenHelper {
    //Esta clase es para crear y conectar la app con la BBDD mediante las constantes de StatusContract

    private static final String TAG = DbHelper.class.getSimpleName();

    public DbHelper(Context context) {
        //el nombre de la BBDD y la versión
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table status (id int primari key, user text, message text, created_at int)
        String sql = String.format("create table %s (%s int primary key, %s text, %s text, %s int)",
                    StatusContract.TABLE,
                    StatusContract.Column.ID,
                    StatusContract.Column.USER,
                    StatusContract.Column.MESSAGE,
                    StatusContract.Column.CREATED_AT);

        Log.d(TAG, "onCreate con SQL: " + sql); //comprobar en Logcat que la tabla se ha creado

        db.execSQL(sql); //Se utiliza execsql porque solo se utiliza una vez (la primera vez que se crea)
                         //Para hacer insercción (repetidamente) se realizará de forma segura con ContentValues
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //sentencias para modificar tabla (ALTER TABLE) --> caso sencillo, se borra y se crea nueva
        db.execSQL("drop table if exists " + StatusContract.TABLE); //borra la vieja base de datos en caso de tener versión nueva
        onCreate(db); //Nueva BBDD
        Log.d(TAG, "onUpgrade"); //comprobar en Logcat que se ha realizado la modificación

    }
}
