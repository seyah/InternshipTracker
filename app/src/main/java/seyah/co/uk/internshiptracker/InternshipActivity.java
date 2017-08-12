package seyah.co.uk.internshiptracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import seyah.co.uk.internshiptracker.db.InternshipContract;
import seyah.co.uk.internshiptracker.db.InternshipDbHelper;
import seyah.co.uk.internshiptracker.pojo.ApplicationStatus;
import seyah.co.uk.internshiptracker.pojo.Internship;

public class InternshipActivity extends AppCompatActivity {

    private InternshipDbHelper mHelper;
    private Internship internship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internship);

        mHelper = new InternshipDbHelper(this);
        updateUI();
    }

    private void updateUI() {
        internship = (Internship) this.getIntent().getSerializableExtra("internship");

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(InternshipContract.InternshipEntry.TABLE,
                new String[]{InternshipContract.InternshipEntry._ID, InternshipContract.InternshipEntry.COL_INTERNSHIP_NAME, InternshipContract.InternshipEntry.COL_PROGRESS},
                InternshipContract.InternshipEntry._ID + " = ?", new String[]{String.valueOf(internship.getId())}, null, null, null);
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(InternshipContract.InternshipEntry._ID);
            int nameIndex = cursor.getColumnIndex(InternshipContract.InternshipEntry.COL_INTERNSHIP_NAME);
            int progressIndex = cursor.getColumnIndex(InternshipContract.InternshipEntry.COL_PROGRESS);

            ApplicationStatus status = ApplicationStatus.valueOf(cursor.getString(progressIndex));

            TextView internshipName = (TextView) this.findViewById(R.id.internship_name);
            internshipName.setText(cursor.getString(nameIndex));
            TextView internshipProgressText = (TextView) this.findViewById(R.id.progress_text);
            internshipProgressText.setText(ApplicationStatus.statusAsPercentage(status)+"%");
            SeekBar internshipProgressBar = (SeekBar) this.findViewById(R.id.progress_bar);
            internshipProgressBar.setMax(100);
            internshipProgressBar.setProgress(ApplicationStatus.statusAsPercentage(status));
            internshipProgressBar.setEnabled(false);
        }


        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.internship_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void deleteInternship(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
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
                startActivity(new Intent(this.getBaseContext(), InternshipsActivity.class));
                break;
            case R.id.action_trash:
                deleteInternship();
                startActivity(new Intent(this.getBaseContext(), InternshipsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
