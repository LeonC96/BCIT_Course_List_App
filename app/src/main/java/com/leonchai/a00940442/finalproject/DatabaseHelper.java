package com.leonchai.a00940442.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Leon_ on 7/16/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getName();
    private static final int SCHEMA_VERSION = 1;
    private static final String DB_NAME = "courses.db";

    private static final String COURSE_TABLE_NAME = "course";
    private static final String ID_COLUMN_NAME = "_id";
    public static final String COURSE_NUMBER_COLUMN_NAME = "course_number";
    public static final String COURSE_NAME_COLUMN_NAME = "course_name";
    public static final String DESCRIPTION_COLUMN_NAME = "description";
    public static final String PREREQ_COLUMN_NAME = "prerequisite";
    public static final String CREDIT_COLUMN_NAME = "credit";

    private static final String CLASS_TABLE_NAME = "class";
    public static final String TERM_COLUMN_NAME = "term";
    public static final String DURATION_COLUMN_NAME = "dates";
    public static final String TIME_COLUMN_NAME = "time";
    public static final String DAYS_COLUMN_NAME = "days";
    public static final String COST_COLUMN_NAME = "cost";
    public static final String INSTRUCTOR_COLUMN_NAME = "instructor";
    public static final String CAMPUS_COLUMN_NAME = "campus";
    public static final String NOTES_COLUMN_NAME = "notes";

    private static DatabaseHelper instance;

    private DatabaseHelper(final Context ctx)
    {
        super(ctx, DB_NAME, null, SCHEMA_VERSION);
    }

    public synchronized static DatabaseHelper getInstance(final Context context)
    {
        if(instance == null)
        {
            instance = new DatabaseHelper(context.getApplicationContext());
        }

        return instance;
    }

    @Override
    public void onConfigure(final SQLiteDatabase db)
    {
        super.onConfigure(db);

        setWriteAheadLoggingEnabled(true);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(final SQLiteDatabase db)
    {
        final String CREATE_NAME_TABLE;

        CREATE_NAME_TABLE = "CREATE TABLE IF NOT EXISTS "  + COURSE_TABLE_NAME + " ( " +
                ID_COLUMN_NAME   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COURSE_NUMBER_COLUMN_NAME + " TEXT NOT NULL, " +
                COURSE_NAME_COLUMN_NAME + " TEXT NOT NULL, " +
                CREDIT_COLUMN_NAME + " TEXT, " +
                DESCRIPTION_COLUMN_NAME + " TEXT, " +
                PREREQ_COLUMN_NAME + " TEXT)";
        db.execSQL(CREATE_NAME_TABLE);

        createClassTable(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db,
                          final int oldVersion,
                          final int newVersion)
    {
    }

    public long getNumberOfCourses(final SQLiteDatabase db)
    {
        final long numEntries;

        numEntries = DatabaseUtils.queryNumEntries(db, COURSE_TABLE_NAME);

        return (numEntries);
    }

    public void insertCourse(final SQLiteDatabase db,
                           final String         courseNumber,
                           final String         courseName)
    {
        final ContentValues contentValues;

        contentValues = new ContentValues();
        contentValues.put(COURSE_NUMBER_COLUMN_NAME, courseNumber);
        contentValues.put(COURSE_NAME_COLUMN_NAME,  courseName);

        db.insert(COURSE_TABLE_NAME, null, contentValues);
    }

    public void updateCourse(final SQLiteDatabase db, Course course){
        final ContentValues cv;

        cv = new ContentValues();
        cv.put(CREDIT_COLUMN_NAME, course.getCredit());
        cv.put(DESCRIPTION_COLUMN_NAME, course.getDescription());
        cv.put(PREREQ_COLUMN_NAME, course.getPrerequisite());

        db.update(COURSE_TABLE_NAME, cv, COURSE_NUMBER_COLUMN_NAME + "= ?", new String[] {course.getCourseNumber()});

    }

    public Cursor getCourse(final SQLiteDatabase db, String courseNumber){
        final Cursor cursor;
        String query;

        query = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COURSE_NUMBER_COLUMN_NAME + "='"
                + courseNumber + "'";

        cursor = db.rawQuery(query, null);

        return cursor;
    }

    public int deleteCourse(final SQLiteDatabase db,
                          final String         courseNumber,
                          final String         courseName)
    {
        final int rows;

        rows = db.delete(COURSE_TABLE_NAME,
                COURSE_NUMBER_COLUMN_NAME + " = ? AND " + COURSE_NAME_COLUMN_NAME + " = ?",
                new String[]
                        {
                                courseNumber,
                                courseName,
                        });

        return (rows);
    }

    public Cursor getAllCourseNames(final SQLiteDatabase db)
    {
        final Cursor cursor;

        String query = "SELECT * FROM " + COURSE_TABLE_NAME;

        cursor = db.rawQuery(query, null);

        return (cursor);
    }

    public void createClassTable(final SQLiteDatabase db){

        final String CREATE_NAME_TABLE;

        CREATE_NAME_TABLE = "CREATE TABLE IF NOT EXISTS "  + CLASS_TABLE_NAME + " ( " +
                ID_COLUMN_NAME   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COURSE_NUMBER_COLUMN_NAME + " TEXT NOT NULL, " +
                TERM_COLUMN_NAME + " TEXT, " +
                DURATION_COLUMN_NAME + " TEXT, " +
                TIME_COLUMN_NAME + " TEXT, " +
                DAYS_COLUMN_NAME + " TEXT, " +
                COST_COLUMN_NAME + " TEXT, " +
                INSTRUCTOR_COLUMN_NAME + " TEXT, " +
                CAMPUS_COLUMN_NAME + " TEXT, " +
                NOTES_COLUMN_NAME + " TEXT)";

        db.execSQL(CREATE_NAME_TABLE);

    }

    public void insertClass(final SQLiteDatabase db,
                             final Class courseClass)
    {
        final ContentValues contentValues;

        contentValues = new ContentValues();
        contentValues.put(COURSE_NUMBER_COLUMN_NAME, courseClass.getCourseNumber());
        contentValues.put(TERM_COLUMN_NAME,  courseClass.getTerm());
        contentValues.put(DURATION_COLUMN_NAME,  courseClass.getDates());
        contentValues.put(TIME_COLUMN_NAME,  courseClass.getTimes());
        contentValues.put(INSTRUCTOR_COLUMN_NAME,  courseClass.getInstructor());
        contentValues.put(DAYS_COLUMN_NAME,  courseClass.getDays());
        contentValues.put(COST_COLUMN_NAME,  courseClass.getCost());
        contentValues.put(CAMPUS_COLUMN_NAME,  courseClass.getCampus());
        contentValues.put(NOTES_COLUMN_NAME,  courseClass.getNotes());

        db.insert(CLASS_TABLE_NAME, null, contentValues);
    }

    public Cursor getClasses(final SQLiteDatabase db, Course course){
        final Cursor cursor;
        String query;

        query = "SELECT * FROM " + CLASS_TABLE_NAME + " WHERE " + COURSE_NUMBER_COLUMN_NAME + "='"
                + course.getCourseNumber() + "'";

        cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor getOneClass(final SQLiteDatabase db, String courseNumber, String duration){
        final Cursor cursor;
        String query;

        query = "SELECT * FROM " + CLASS_TABLE_NAME + " WHERE " + COURSE_NUMBER_COLUMN_NAME + "='"
                + courseNumber + "' AND " + DURATION_COLUMN_NAME + "='" + duration + "'";

        cursor = db.rawQuery(query, null);

        return cursor;
    }
}
