package android.bignerdranch.com.database;

import android.bignerdranch.com.database.CrimeDBschema.CrimeTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";
    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
//users/androidstudio//etc/main/java/bnerd.com/databases
    //updates
    //debugging: delete app off device to destroy databases, Run to recreate them
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeTable.NAME + "("
                + " _id integer primary key autoincrement, "
                + CrimeTable.Cols.UUID
                + ", "
                + CrimeTable.Cols.TITLE
                + ", "
                + CrimeTable.Cols.DATE
                + ", "
                + CrimeTable.Cols.SOLVED
                + ", "
                + CrimeTable.Cols.SUSPECT
                + ")"

    );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
//14.7 in textbook
//go from gutting