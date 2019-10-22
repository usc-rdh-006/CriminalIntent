package android.bignerdranch.com;

import android.bignerdranch.com.database.CheckInBaseHelper;
import android.bignerdranch.com.database.CheckInCursorWrapper;
import android.bignerdranch.com.database.CheckInDBschema;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckInLab {
    private static CheckInLab sCheckInLab;

    private Context mContext;

    private SQLiteDatabase mDatabase;

    public static CheckInLab get(Context context) {
        if (sCheckInLab == null) {
            sCheckInLab = new CheckInLab(context);
        }
        return sCheckInLab;
    }


    private CheckInLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CheckInBaseHelper(mContext)
                .getWritableDatabase();


    }


    //this (addCrime) fills out crime with new implementation (database)
    //mdatabase insert has two arguments (plus a nullcolumnHack)
    //first agrument is name of table, third is data to insert

    public void addCrime(MyCheckIn c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CheckInDBschema.CrimeTable.NAME,
                null, values);

    }
    public int deleteCrime(MyCheckIn myCheckIn) {
        String uuidString = myCheckIn.getId().toString();
        return mDatabase.delete(
                CheckInDBschema.CrimeTable.NAME,
                CheckInDBschema.CrimeTable.Cols.UUID + " = ?",
                new String[] { uuidString }
        );
    }







    public List<MyCheckIn> getCrimes() {
//walk cursor and populate crime list
        List<MyCheckIn> myCheckIns = new ArrayList<>();
        CheckInCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                myCheckIns.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return myCheckIns;
    }

    public MyCheckIn getCrime(UUID id) {

        CheckInCursorWrapper cursor = queryCrimes(
                CheckInDBschema.CrimeTable.Cols.UUID + " = ?", new String[] {
                        id.toString() }
                        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(MyCheckIn myCheckIn) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, myCheckIn.getPhotoFilename());
    }

    //Can insert crimes, so the code that adds MyCheckIn to CheckInLab when you press the New MyCheckIn action item now works
    //Can successfully query the database, CheckInPagerActivity can see all the Crimes in CheckInLab
    //CheckInLab.getCrime(UUID) works so each CheckInFragment displayed in CheckInPagerActivity is showing the real MyCheckIn






    //update myCheckIn works similarly to an insert -
    // pass table name for updating and the ContentValues to assign to each updated row
    public void updateCrime(MyCheckIn myCheckIn) {
        String uuidString = myCheckIn.getId().toString();
        ContentValues values = getContentValues(myCheckIn);
        mDatabase.update(CheckInDBschema.CrimeTable.NAME, values, CheckInDBschema.CrimeTable.Cols.UUID + " = ?", new String[] {
                uuidString
        });
    }


    private CheckInCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CheckInDBschema.CrimeTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
                 );

        return new CheckInCursorWrapper(cursor);
    }

    //puts crimes into contentvalues - contentvalues help write and update databases
    //contentvalues is a key-value store class (like bundles) but this one is specifically designed for
    //sqllite
    private static ContentValues getContentValues(MyCheckIn myCheckIn) {
        ContentValues values = new ContentValues();
        values.put(CheckInDBschema.CrimeTable.Cols.UUID, myCheckIn.getId().toString());
        values.put(CheckInDBschema.CrimeTable.Cols.TITLE, myCheckIn.getTitle());
        values.put(CheckInDBschema.CrimeTable.Cols.DATE, myCheckIn.getDate().getTime());
        values.put(CheckInDBschema.CrimeTable.Cols.SOLVED, myCheckIn.isSolved() ? 1 : 0);
        values.put(CheckInDBschema.CrimeTable.Cols.SUSPECT, myCheckIn.getSuspect());
        return values;
    }

}
