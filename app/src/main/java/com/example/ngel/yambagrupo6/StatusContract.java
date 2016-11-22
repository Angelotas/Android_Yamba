package com.example.ngel.yambagrupo6;


import android.net.Uri;
import android.provider.BaseColumns;
/**
 * Created by Ángel on 16/11/2016.
 */

public class StatusContract {
    //si se almacenan las constantes en esta clase, al modificar la BBDD no habrá que modificar mas archivos que este
    //CONSTANTES DE LA BBDD
    public static final String DB_NAME = "timeline.db";  //nombre de la BBDD
    public static final int DB_VERSION = 1;              //versión de la BBDD (importante ante actualizaciones del esquema)
    public static final String TABLE = "status";         //tabla status
    public static final String DEFAULT_SORT = Column.CREATED_AT + " DESC";  //como irá ordenado por defecto

    //CONSTANTES PARA EL CONTENT PROVIDER
    public static final String AUTHORITY = "com.example.ngel.yambagrupo6.StatusProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    //dos tipos de uris que hay
    public static final int STATUS_ITEM = 1;  //-->aceso a un elemento con ID (STATUS_ITEM)
    public static final int STATUS_DIR = 2; //-->aceso genérico a una tabla (STATUS_DIR)


    public class Column { //subclase de las columnas de la tabla status
        public static final String ID = BaseColumns._ID;        //identificador del tweet
        public static final String USER = "user";               //usuario que escibe el tweet
        public static final String MESSAGE = "message";         //texto del tweet
        public static final String CREATED_AT = "created_at";   //fecha de creación del tweet
    }

}
