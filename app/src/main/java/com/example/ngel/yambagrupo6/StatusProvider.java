package com.example.ngel.yambagrupo6;

/**
 * Created by Ángel on 22/11/2016.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

public class StatusProvider extends ContentProvider{

    private static final String TAG = StatusProvider.class.getSimpleName();
    private DbHelper dbHelper;
    SQLiteDatabase db;
    private static final UriMatcher sURIMatcher; //métodos UriMatcher (para interpretar uri de la llamada)

    static {
        sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE , StatusContract.STATUS_DIR); //uri hace referencia a un directorio
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE + "/#", StatusContract.STATUS_ITEM); //hace referencia a registro de la tabla
    }




    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        Log.d(TAG, "onCreated");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri ret = null; //uri que se devuelve si se inserta correctamente

        //uri correcta?
        if (sURIMatcher.match(uri) != StatusContract.STATUS_DIR) { //si la uri no es de tipo directorio es incorrecta (no sabemos el id para insertar)
            throw new IllegalArgumentException("uri incorrecta: " + uri);
        }

        db = dbHelper.getWritableDatabase();    //abre la bbdd como escritura
        //ahora se inserta la información contenida en el contentValues
        long rowId = db.insertWithOnConflict(StatusContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        //se inserta correctamente?
        if (rowId != -1){ //en caso de que si --> construir URI a devolver y se notifica
            long id = values.getAsLong(StatusContract.Column.ID);
            ret = ContentUris.withAppendedId(uri, id); //se construye nueva uri
            Log.d(TAG, "uri insertada: " + ret);

            // Notificar que los datos para la URI han cambiado
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ret;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String where; //todo lo que va dentro de where en la clausula SQL update

        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                where = selection;
                break;
            case StatusContract.STATUS_ITEM:  //actualización de un registro concreto de la bbdd
                long id = ContentUris.parseId(uri); //conseguimos la id que se ha pasado por la uri
                where = StatusContract.Column.ID
                        + "="
                        + id
                        + (TextUtils.isEmpty(selection) ? "" : " and ( " + selection + " )");
                break;
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        //se ejecuta sentencia de modificación de bbdd
        int ret = db.update(StatusContract.TABLE, values, where, selectionArgs);

        if (ret > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d(TAG, "registros actualizados: " + ret);
        return ret;


    }
}
