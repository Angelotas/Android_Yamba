package com.example.ngel.yambagrupo6;


import android.provider.BaseColumns;
/**
 * Created by Ángel on 16/11/2016.
 */

public class StatusContract {
    //Esta clase se declarará la información de la BBDD (constantes de la misma)
    //si se almacenan las constantes en esta clase, al modificar la BBDD no habrá que modificar mas archivos que este
    public static final String DB_NAME = "timeline.db";  //nombre de la BBDD
    public static final int DB_VERSION = 1;              //versión de la BBDD (importante ante actualizaciones del esquema)
    public static final String TABLE = "status";         //tabla status

    public static final String DEFAULT_SORT = Column.CREATED_AT + " DESC";  //como irá ordenado por defecto

    public class Column { //subclase de las columnas de la tabla status
        public static final String ID = BaseColumns._ID;        //identificador del tweet
        public static final String USER = "user";               //usuario que escibe el tweet
        public static final String MESSAGE = "message";         //texto del tweet
        public static final String CREATED_AT = "created_at";   //fecha de creación del tweet
    }

}
