package com.example.ngel.yambagrupo6;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Ángel on 23/11/2016.
 */
public class TimelineFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = TimelineFragment.class.getSimpleName();

    private SimpleCursorAdapter mAdapter; //variable del adapter

    //Las columnas de la bbdd desde las que el adapter va a mapear los datos
    private static final String[] FROM = {StatusContract.Column.USER,
            StatusContract.Column.MESSAGE, StatusContract.Column.CREATED_AT};
    //Hace referencia al fichero list_item.xml donde el adapter pondrá los datos  (mismo orden en FROM y TO
    private static final int[] TO = {R.id.list_item_text_user,
            R.id.list_item_text_message, R.id.list_item_text_created_at};

    private static final int LOADER_ID = 42; //para saber que loader estamos usando en caso de tener varios

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item, null, FROM, TO, 0); //adapter que mapea los datos del
                                                                                                  //cursor a los elementos list_item.xml
        setListAdapter(mAdapter);   //se asocia el adapter al ListView del fragment

        getLoaderManager().initLoader(LOADER_ID,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //método que hace todo el proceso de carga desde la bbdd
        if (id != LOADER_ID)
            return null;
        Log.d(TAG, "onCreateLoader");
        //Carga de los datos desde el content provider
        return new CursorLoader(getActivity(), StatusContract.CONTENT_URI, null, null, null, StatusContract.DEFAULT_SORT);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //Una vez que los datos ya estan cargados, tenemos el control del cursor ya recargado de datos del contentProvider
        Log.d(TAG, "onLoadFinished with cursor: " + cursor.getCount());
        mAdapter.swapCursor(cursor);//actualiza los datos que el adapter está usando para mostrar en la lista

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
