package grover.Buhlo13;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by ndv on 09.09.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {
    // имя базы данных
    public static final String DATABASE_NAME = "costsDB.db";
    // версия базы данных
    private static final int DATABASE_VERSION = 1;
    // имя таблицы
    private static final String DATABASE_TABLE = "items";
    // названия столбцов
    public static final String NAME_COLUMN = "name";
    public static final String PRICE_COLUMN = "price";
    public static final String DATE_COLUMN = "date";
    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + getDatabaseTable() + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + NAME_COLUMN
            + " text not null, " + PRICE_COLUMN + " integer, " + DATE_COLUMN
            + " integer);";




    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static String getDatabaseTable() {
        return DATABASE_TABLE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //db.execSQL("DROP TABLE IF EXISTS " + getDatabaseTable());
        //onCreate(db);
    }

}
