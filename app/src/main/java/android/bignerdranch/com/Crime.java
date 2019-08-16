package android.bignerdranch.com;

import java.util.Date;
import java.util.UUID;


//UUID is a Java utility class included in the Android framework. It provides an easy way to generate universally unique ID values.
// In the constructor you generate a random unique ID by calling UUID.randomUUID().

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

//Initializing the Date variable using the default Date constructor sets mDate to the current date.
// This will be the default date for a crime.

    //get and set

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
//At this point, you have created the model layer
//and an activity that is capable of hosting a support fragment

//To host a UI fragment, an activity must:
//• define a spot in its layout for the fragment’s view
//• manage the lifecycle of the fragment instance