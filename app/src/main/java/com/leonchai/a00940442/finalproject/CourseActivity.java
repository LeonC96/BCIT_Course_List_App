package com.leonchai.a00940442.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;

    private TextView nameTextView;
    private TextView numberTextView;
    private TextView creditTextView;
    private TextView descriptionTextView;
    private TextView prereqTextView;
    private ListView classListView;

    private Course course;
    private ArrayList<String> classes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        final Intent intent = getIntent();
        course = new Course();

        course.setCourseNumber(intent.getStringExtra("number"));
        course.setCourseName(intent.getStringExtra("name"));

        mDatabaseHelper = DatabaseHelper.getInstance(getApplicationContext());

        nameTextView = (TextView) findViewById(R.id.courseNameTextView);
        numberTextView = (TextView) findViewById(R.id.courseNumberTextView);
        creditTextView = (TextView) findViewById(R.id.creditTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptiontextView);
        prereqTextView = (TextView) findViewById(R.id.prereqTextView);

        descriptionTextView.setMovementMethod(new ScrollingMovementMethod());


        classListView = (ListView) findViewById(R.id.classListView);

        classListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent classIntent;
                String oneClass;
                classIntent = new Intent(CourseActivity.this, ClassActivity.class);

                oneClass = classes.get(position);

                classIntent.putExtra("class", oneClass);
                classIntent.putExtra("courseNumber", course.getCourseNumber());

                startActivity(classIntent);

            }
        });


        checkCourse();
    }

    private void checkCourse(){
        final SQLiteDatabase db;
        Cursor cursor;

        db = mDatabaseHelper.getReadableDatabase();

        cursor = mDatabaseHelper.getCourse(db, course.getCourseNumber());

        cursor.moveToFirst();

        if(cursor.isNull(cursor.getColumnIndex(DatabaseHelper.DESCRIPTION_COLUMN_NAME)) ||
                cursor.isNull(cursor.getColumnIndex(DatabaseHelper.CREDIT_COLUMN_NAME))){
            new MyTask().execute();
        } else {
            display();
        }


    }

    private void display(){
        final SQLiteDatabase db;
        Cursor cursor;
        ArrayAdapter<String> adapter;

        db = mDatabaseHelper.getReadableDatabase();

        cursor = mDatabaseHelper.getCourse(db, course.getCourseNumber());

        cursor.moveToFirst();


        course.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESCRIPTION_COLUMN_NAME)));
        course.setPrerequisite(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PREREQ_COLUMN_NAME)));
        course.setCredit(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CREDIT_COLUMN_NAME)));

        nameTextView.setText(course.getCourseName());
        numberTextView.setText(course.getCourseNumber());
        creditTextView.append(course.getCredit());
        descriptionTextView.setText(course.getDescription());
        prereqTextView.setText(course.getPrerequisite());

        cursor = mDatabaseHelper.getClasses(db, course);


        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                classes.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DURATION_COLUMN_NAME)));
                cursor.moveToNext();
            }
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classes);
        classListView.setAdapter(adapter);




    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            course.setDescription(Scraper.getCourseDescription(course.getCourseNumber()));
            course.setPrerequisite(Scraper.getCoursePrereq(course));
            course.setCredit(Scraper.getCourseCredit(course));
            course.setTerms(Scraper.getTerms(course));

            if(!course.getTerms().isEmpty()){
                course.setClasses(Scraper.getClasses(course));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            final SQLiteDatabase db;

            db = mDatabaseHelper.getWritableDatabase();

            db.beginTransaction();

            mDatabaseHelper.updateCourse(db, course);

            if(course.getClasses() != null && !course.getClasses().isEmpty()){
                for(Class c : course.getClasses()){
                    mDatabaseHelper.insertClass(db, c);
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            display();
        }
    }
}
