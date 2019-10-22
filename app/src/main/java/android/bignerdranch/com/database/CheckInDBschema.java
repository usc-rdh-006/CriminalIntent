package android.bignerdranch.com.database;

public class CheckInDBschema {
    public static final class CrimeTable {
        public static final String NAME = "CheckIns";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "complete";
            public static final String SUSPECT = "contact";
        }
    }
}
