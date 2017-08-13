package seyah.co.uk.internshiptracker;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import seyah.co.uk.internshiptracker.adapter.InternshipEventRowAdapter;
import seyah.co.uk.internshiptracker.adapter.InternshipRowAdapter;
import seyah.co.uk.internshiptracker.db.InternshipContract;
import seyah.co.uk.internshiptracker.db.InternshipDbHelper;
import seyah.co.uk.internshiptracker.pojo.ApplicationStatus;
import seyah.co.uk.internshiptracker.pojo.Internship;
import seyah.co.uk.internshiptracker.pojo.InternshipEvent;

public class InternshipActivity extends AppCompatActivity {

    private InternshipDbHelper mHelper;
    private Internship internship;
    private ListView mTaskListView;
    private InternshipEventRowAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internship);

        mHelper = new InternshipDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.events_list);

        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = mTaskListView.getItemAtPosition(position);
                InternshipEvent fullObject = (InternshipEvent)o;
            }
        });


        final FloatingActionButton addInternship = (FloatingActionButton) findViewById(R.id.add_internship_event);
        final View dialogView = LayoutInflater.from(InternshipActivity.this).inflate(R.layout.new_internship_event, null);
        final EditText eventName = (EditText) dialogView.findViewById(R.id.new_event_name);
        final EditText eventDescription = (EditText) dialogView.findViewById(R.id.new_event_description);
        final Spinner eventStatus = (Spinner) dialogView.findViewById(R.id.new_event_status);
        final DatePicker eventDate = (DatePicker) dialogView.findViewById(R.id.new_event_date);
        final CheckBox eventNotify = (CheckBox) dialogView.findViewById(R.id.new_event_notify);

        final AlertDialog.Builder builder = new AlertDialog.Builder(InternshipActivity.this);
        builder.setTitle("Add Event");
        builder.setView(dialogView);
        builder.setCancelable(false);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                Calendar calendar = new GregorianCalendar(eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth());

                values.put(InternshipContract.InternshipEventEntry.COL_EVENT_NAME, eventName.getText().toString());
                values.put(InternshipContract.InternshipEventEntry.COL_INTERNSHIP_ID, internship.getId());
                values.put(InternshipContract.InternshipEventEntry.COL_DESCRIPTION, eventDescription.getText().toString());
                values.put(InternshipContract.InternshipEventEntry.COL_DATE, calendar.getTimeInMillis());
                values.put(InternshipContract.InternshipEventEntry.COL_NOTIFY, eventNotify.isChecked());
                values.put(InternshipContract.InternshipEventEntry.COL_STATUS, ApplicationStatus.valueOf(eventStatus.getSelectedItem().toString().replace(" ", "_").toUpperCase()).toString());
                db.insertWithOnConflict(InternshipContract.InternshipEventEntry.TABLE,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_ABORT);
                db.close();
                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        addInternship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                builder.show();
            }
        });

        updateUI();
    }

    private void updateUI() {
        internship = (Internship) this.getIntent().getSerializableExtra("internship");
        TextView internshipName = (TextView) this.findViewById(R.id.internship_name);
        TextView internshipProgressText = (TextView) this.findViewById(R.id.progress_text);
        SeekBar internshipProgressBar = (SeekBar) this.findViewById(R.id.progress_bar);

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(InternshipContract.InternshipEntry.TABLE,
                new String[]{InternshipContract.InternshipEntry._ID, InternshipContract.InternshipEntry.COL_INTERNSHIP_NAME, InternshipContract.InternshipEntry.COL_PROGRESS},
                InternshipContract.InternshipEntry._ID + " = ?", new String[]{String.valueOf(internship.getId())}, null, null, null);
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(InternshipContract.InternshipEntry._ID);
            int nameIndex = cursor.getColumnIndex(InternshipContract.InternshipEntry.COL_INTERNSHIP_NAME);
            int progressIndex = cursor.getColumnIndex(InternshipContract.InternshipEntry.COL_PROGRESS);

            ApplicationStatus status = ApplicationStatus.valueOf(cursor.getString(progressIndex));

            internshipName.setText(cursor.getString(nameIndex));
            internshipProgressBar.setMax(100);
            internshipProgressBar.setProgress(ApplicationStatus.statusAsPercentage(status));
            internshipProgressBar.setEnabled(false);
        }

        cursor.close();

        ArrayList<InternshipEvent> internshipEventList = new ArrayList<>();

        Cursor cursor2 = db.query(InternshipContract.InternshipEventEntry.TABLE,
                new String[]{
                        InternshipContract.InternshipEventEntry._ID,
                        InternshipContract.InternshipEventEntry.COL_EVENT_NAME,
                        InternshipContract.InternshipEventEntry.COL_INTERNSHIP_ID,
                        InternshipContract.InternshipEventEntry.COL_STATUS,
                        InternshipContract.InternshipEventEntry.COL_DESCRIPTION,
                        InternshipContract.InternshipEventEntry.COL_DATE,
                        InternshipContract.InternshipEventEntry.COL_NOTIFY,
                },
                InternshipContract.InternshipEventEntry.COL_INTERNSHIP_ID + " = ?",
                new String[]{String.valueOf(internship.getId())}, null, null, null);

        int progress = 0;

        while(cursor2.moveToNext()) {
            int idIndex = cursor2.getColumnIndex(InternshipContract.InternshipEventEntry._ID);
            int nameIndex = cursor2.getColumnIndex(InternshipContract.InternshipEventEntry.COL_EVENT_NAME);
            int internshipIndex = cursor2.getColumnIndex(InternshipContract.InternshipEventEntry.COL_INTERNSHIP_ID);
            int descriptionIndex = cursor2.getColumnIndex(InternshipContract.InternshipEventEntry.COL_DESCRIPTION);
            int statusIndex = cursor2.getColumnIndex(InternshipContract.InternshipEventEntry.COL_STATUS);
            int dateIndex = cursor2.getColumnIndex(InternshipContract.InternshipEventEntry.COL_DATE);
            int notifyIndex = cursor2.getColumnIndex(InternshipContract.InternshipEventEntry.COL_NOTIFY);

            InternshipEvent internshipEvent = new InternshipEvent(cursor2.getInt(idIndex),
                    cursor2.getString(nameIndex),
                    cursor2.getInt(internshipIndex),
                    cursor2.getString(descriptionIndex),
                    ApplicationStatus.valueOf(cursor2.getString(statusIndex)),
                    new Date(cursor2.getLong(dateIndex)),
                    cursor2.getInt(notifyIndex) > 0);
            internshipEventList.add(internshipEvent);

            if(progress < ApplicationStatus.statusAsPercentage(ApplicationStatus.valueOf(cursor2.getString(statusIndex)))) {
                progress = ApplicationStatus.statusAsPercentage(ApplicationStatus.valueOf(cursor2.getString(statusIndex)));
            }
        }

        internshipProgressText.setText(progress + "%");
        internshipProgressBar.setProgress(progress);
        Collections.sort(internshipEventList);

        if (mAdapter == null) {
            mAdapter = new InternshipEventRowAdapter(this, internshipEventList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(internshipEventList);
            mAdapter.notifyDataSetChanged();
        }

        cursor2.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.internship_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void deleteInternship() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Log.d("TTT", internship.getId()+"");
        db.delete(InternshipContract.InternshipEventEntry.TABLE,
                InternshipContract.InternshipEventEntry.COL_INTERNSHIP_ID + " = ?",
                new String[]{String.valueOf(internship.getId())});
        db.delete(InternshipContract.InternshipEntry.TABLE,
                InternshipContract.InternshipEntry._ID + " = ?",
                new String[]{String.valueOf(internship.getId())});
        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_go_back:
                super.finish();
                break;
            case R.id.action_trash:
                deleteInternship();
                super.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteEvent(View view) {
        View parent = (View) view.getParent().getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.internship_event_id);
        String eventId = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(InternshipContract.InternshipEventEntry.TABLE,
                InternshipContract.InternshipEventEntry._ID + " = ?",
                new String[]{eventId});
        db.close();
        updateUI();
    }
}
