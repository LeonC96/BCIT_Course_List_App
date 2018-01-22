package com.leonchai.a00940442.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ClassActivity extends AppCompatActivity {

    private String classTime;
    private String courseNumber;

    private DatabaseHelper mDatabaseHelper;

    private Class mClass;

    private TextView courseNumberTV;
    private TextView durationTV;
    private TextView timeTV;
    private TextView dayTV;
    private TextView instructorTV;
    private TextView campusTV;
    private TextView costTV;
    private TextView noteTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        final Intent intent;

        intent = getIntent();

        mDatabaseHelper = DatabaseHelper.getInstance(getApplicationContext());

        mClass = new Class();

        classTime = intent.getStringExtra("class");
        courseNumber = intent.getStringExtra("courseNumber");

        courseNumberTV =(TextView) findViewById(R.id.courseNumberTextView);
        durationTV =(TextView) findViewById(R.id.durationTextView);
        timeTV =(TextView) findViewById(R.id.timeTextView);
        dayTV =(TextView) findViewById(R.id.dayTextView);
        instructorTV =(TextView) findViewById(R.id.instructorTextView);
        campusTV =(TextView) findViewById(R.id.campusTextView);
        costTV =(TextView) findViewById(R.id.costTextView);
        noteTV =(TextView) findViewById(R.id.noteTextView);

        getCourseClass();
    }

    private void getCourseClass(){
        final SQLiteDatabase db;
        Cursor cursor;

        db = mDatabaseHelper.getReadableDatabase();

        cursor = mDatabaseHelper.getOneClass(db, courseNumber, classTime);

        cursor.moveToFirst();

        mClass.setDates(classTime);
        mClass.setCourseNumber(courseNumber);
        mClass.setCost(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COST_COLUMN_NAME)));
        mClass.setNotes(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOTES_COLUMN_NAME)));
        mClass.setTimes(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME_COLUMN_NAME)));
        mClass.setCampus(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CAMPUS_COLUMN_NAME)));
        mClass.setInstructor(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INSTRUCTOR_COLUMN_NAME)));
        mClass.setDays(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAYS_COLUMN_NAME)));


        courseNumberTV.setText(mClass.getCourseNumber());
        durationTV.setText(mClass.getDates());
        timeTV.setText(mClass.getTimes());
        dayTV.setText(mClass.getDays());
        instructorTV.setText(mClass.getInstructor());
        campusTV.setText(mClass.getCampus());
        costTV.setText(mClass.getCost());
        noteTV.setText(mClass.getNotes());
    }
}
