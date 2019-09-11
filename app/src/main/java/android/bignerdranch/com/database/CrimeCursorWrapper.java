package android.bignerdranch.com.database;

import android.bignerdranch.com.Crime;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;


// a cursor subclass that reads data from cursor
//lets us wrap a cursor from another place and add new methods on top
//in this application it will pull out relevant column data

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }


    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeDBschema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDBschema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDBschema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDBschema.CrimeTable.Cols.SOLVED));
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);

        return crime;
    }
}