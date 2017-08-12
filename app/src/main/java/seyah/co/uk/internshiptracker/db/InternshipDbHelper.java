package seyah.co.uk.internshiptracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InternshipDbHelper extends SQLiteOpenHelper {

    public InternshipDbHelper(Context context) {
        super(context, InternshipContract.DB_NAME, null, InternshipContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createInternshipTable = "CREATE TABLE " + InternshipContract.InternshipEntry.TABLE + " ( " +
                InternshipContract.InternshipEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InternshipContract.InternshipEntry.COL_INTERNSHIP_NAME + " TEXT NOT NULL, " +
                InternshipContract.InternshipEntry.COL_PROGRESS + " TEXT" +
                ");";
        String createEventTable = "CREATE TABLE " + InternshipContract.InternshipEventEntry.TABLE + " ( " +
                InternshipContract.InternshipEventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InternshipContract.InternshipEventEntry.COL_EVENT_NAME + " TEXT NOT NULL," +
                InternshipContract.InternshipEventEntry.COL_INTERNSHIP_ID + " INTEGER NOT NULL," +
                InternshipContract.InternshipEventEntry.COL_DESCRIPTION + "TEXT," +
                InternshipContract.InternshipEventEntry.COL_DATE + "DATE," +
                "FOREIGN KEY(" + InternshipContract.InternshipEventEntry.COL_INTERNSHIP_ID +
                ") REFERENCES " + InternshipContract.InternshipEntry.TABLE +
                "(" + InternshipContract.InternshipEntry._ID + "));";
        db.execSQL(createInternshipTable);
        db.execSQL(createEventTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InternshipContract.InternshipEntry.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + InternshipContract.InternshipEventEntry.TABLE);
        onCreate(db);
    }
}