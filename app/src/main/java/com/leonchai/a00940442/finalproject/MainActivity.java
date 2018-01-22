package com.leonchai.a00940442.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> classNames;
    private ArrayList<String> classNumbers;
    private SimpleCursorAdapter adapter;
    private ListView mListView;
    private Cursor cursor;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classNames = new ArrayList<String>();
        classNumbers = new ArrayList<String>();
        mDatabaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        mListView = (ListView) findViewById(R.id.list);
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{DatabaseHelper.COURSE_NAME_COLUMN_NAME, DatabaseHelper.COURSE_NUMBER_COLUMN_NAME},
                new int[]{android.R.id.text1, android.R.id.text2},
                0);

        mListView.setAdapter(adapter);

        getClassNames();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name;
                String number;
                final Intent intent;

                intent = new Intent(MainActivity.this, CourseActivity.class);
                name = classNames.get(position);
                number = classNumbers.get(position);

                intent.putExtra("name", name);
                intent.putExtra("number", number);

                startActivity(intent);

            }
        });
    }

    private void getClassNames(){
        final SQLiteDatabase db;
        final long entries;

        db = mDatabaseHelper.getWritableDatabase();
        entries = mDatabaseHelper.getNumberOfCourses(db);

        if(entries == 0) {
            new MyTask().execute();
        } else {
            display();
        }
    }

    private void display(){
        final SQLiteDatabase db;

        db     = mDatabaseHelper.getReadableDatabase();
        cursor = mDatabaseHelper.getAllCourseNames(db);

        if(classNames.isEmpty()) {
            while (cursor.moveToNext()) {
                classNames.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_NAME_COLUMN_NAME)));
                classNumbers.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_NUMBER_COLUMN_NAME)));
            }
        }

        adapter.changeCursor(cursor);

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            classNames.addAll(Scraper.getCourseNamesList());
            classNumbers.addAll(Scraper.getCourseNumbersList());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            SQLiteDatabase db;
            db = mDatabaseHelper.getWritableDatabase();

            db.beginTransaction();
            for(int i = 0; i < classNames.size(); i++){
                mDatabaseHelper.insertCourse(db, classNumbers.get(i), classNames.get(i));
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            display();
        }
    }
}
