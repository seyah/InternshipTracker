package seyah.co.uk.internshiptracker.db;

import android.provider.BaseColumns;


public class InternshipContract {
    public static final String DB_NAME = "seyah.co.uk.internshiptracker.db";
    public static final int DB_VERSION = 6;

    public class InternshipEntry implements BaseColumns {
        public static final String TABLE = "internships";

        public static final String COL_INTERNSHIP_NAME = "name";
        public static final String COL_PROGRESS = "progress";
    }

    public class InternshipEventEntry implements BaseColumns {
        public static final String TABLE = "internship_events";

        public static final String COL_EVENT_NAME = "name";
        public static final String COL_INTERNSHIP_ID = "internship";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_STATUS = "status";
        public static final String COL_DATE = "date";
        public static final String COL_NOTIFY = "notify";
    }
}