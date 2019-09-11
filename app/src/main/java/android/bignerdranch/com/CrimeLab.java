package android.bignerdranch.com;

import android.bignerdranch.com.database.CrimeBaseHelper;
import android.bignerdranch.com.database.CrimeCursorWrapper;
import android.bignerdranch.com.database.CrimeDBschema;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private Context mContext;

    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();


    }

    //this (addCrime) fills out crime with new implementation (database)
    //mdatabase insert has two arguments (plus a nullcolumnHack)
    //first agrument is name of table, third is data to insert

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeDBschema.CrimeTable.NAME,
                null, values);

    }






    public List<Crime> getCrimes() {
//walk cursor and populate crime list
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {

        CrimeCursorWrapper cursor = queryCrimes(
                CrimeDBschema.CrimeTable.Cols.UUID + " = ?", new String[] {
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

    //Can insert crimes, so the code that adds Crime to CrimeLab when you press the New Crime action item now works
    //Can successfully query the database, CrimePagerActivity can see all the Crimes in CrimeLab
    //CrimeLab.getCrime(UUID) works so each CrimeFragment displayed in CrimePagerActivity is showing the real Crime






    //update crime works similarly to an insert -
    // pass table name for updating and the ContentValues to assign to each updated row
    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeDBschema.CrimeTable.NAME, values, CrimeDBschema.CrimeTable.Cols.UUID + " = ?", new String[] {
                uuidString
        });
    }


    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeDBschema.CrimeTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
                 );

        return new CrimeCursorWrapper(cursor);
    }

    //puts crimes into contentvalues - contentvalues help write and update databases
    //contentvalues is a key-value store class (like bundles) but this one is specifically designed for
    //sqllite
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDBschema.CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeDBschema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeDBschema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeDBschema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }
}
