package seyah.co.uk.internshiptracker;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import seyah.co.uk.internshiptracker.adapter.InternshipRowAdapter;
import seyah.co.uk.internshiptracker.db.InternshipContract;
import seyah.co.uk.internshiptracker.db.InternshipDbHelper;
import seyah.co.uk.internshiptracker.pojo.ApplicationStatus;
import seyah.co.uk.internshiptracker.pojo.Internship;

public class InternshipsActivity extends AppCompatActivity {

    public static final String TAG = "InternshipsActivity";
    private InternshipDbHelper mHelper;
    private ListView mTaskListView;
    private InternshipRowAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internships);

        mHelper = new InternshipDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_internships);

        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = mTaskListView.getItemAtPosition(position);
                Internship fullObject = (Internship)o;
                Intent intent = new Intent(getBaseContext(), InternshipActivity.class);
                intent.putExtra("internship", fullObject);
                startActivity(intent);
            }
        });

        FloatingActionButton addInternship = (FloatingActionButton) findViewById(R.id.addInternship);
        addInternship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(view.getContext());
                AlertDialog d = new AlertDialog.Builder(view.getContext())
                        .setTitle("Add internship log")
                        .setNegativeButton("Cancel", null)
                        .setView(input)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String internshipName = String.valueOf(input.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(InternshipContract.InternshipEntry.COL_INTERNSHIP_NAME, internshipName);
                                values.put(InternshipContract.InternshipEntry.COL_PROGRESS, ApplicationStatus.FIRST_INTERVIEW.toString());
                                db.insertWithOnConflict(InternshipContract.InternshipEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_ABORT);
                                db.close();
                                updateUI();
                            }
                        }).create();
                d.show();
            }
        });

        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        ArrayList<Internship> internshipList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(InternshipContract.InternshipEntry.TABLE,
                new String[]{InternshipContract.InternshipEntry._ID, InternshipContract.InternshipEntry.COL_INTERNSHIP_NAME, InternshipContract.InternshipEntry.COL_PROGRESS},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(InternshipContract.InternshipEntry._ID);
            int nameIndex = cursor.getColumnIndex(InternshipContract.InternshipEntry.COL_INTERNSHIP_NAME);
            Internship internship = new Internship(cursor.getInt(idIndex), cursor.getString(nameIndex));
            internshipList.add(internship);
        }

        if (mAdapter == null) {
            mAdapter = new InternshipRowAdapter(this, internshipList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(internshipList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
