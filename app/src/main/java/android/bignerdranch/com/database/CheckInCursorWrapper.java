package android.bignerdranch.com.database;

import android.bignerdranch.com.MyCheckIn;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;


// a cursor subclass that reads data from cursor
//lets us wrap a cursor from another place and add new methods on top
//in this application it will pull out relevant column data

public class CheckInCursorWrapper extends CursorWrapper {
    public CheckInCursorWrapper(Cursor cursor) {
        super(cursor);
    }


    public MyCheckIn getCrime() {
        String uuidString = getString(getColumnIndex(CheckInDBschema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CheckInDBschema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CheckInDBschema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CheckInDBschema.CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CheckInDBschema.CrimeTable.Cols.SUSPECT));


        MyCheckIn myCheckIn = new MyCheckIn(UUID.fromString(uuidString));
        myCheckIn.setTitle(title);
        myCheckIn.setDate(new Date(date));
        myCheckIn.setSolved(isSolved != 0);
        myCheckIn.setSuspect(suspect);

        return myCheckIn;
    }
}