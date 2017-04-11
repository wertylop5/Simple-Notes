package com.projectsling.simplenotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity {
    private static final String TAG = CreateActivity.class.getSimpleName();
    private static final String NOTE_ID = "id";
    private static final String NOTE_TITLE = "title";
    private static final String NOTE_BODY = "body";
    private static final String NOTE_CREATED = "created";
    private static final String NOTE_EDITED = "last_edit";


    private EditText mTitleEdit;
    private EditText mBody;
    private Button mCancel;
    private Button mCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mTitleEdit = (EditText)findViewById(R.id.titleEdit);
        mBody = (EditText)findViewById(R.id.bodyText);
        mCancel = (Button)findViewById(R.id.cancelButton);
        mCreate = (Button)findViewById(R.id.createButton);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateActivity.this.finish();
            }
        });

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNote();
            }
        });
    }

    private void writeNote() {
        Log.v(TAG, "Writing note");
        String noteTitle = mTitleEdit.getText().toString();
        String noteBody = mBody.getText().toString();
        Calendar now = Calendar.getInstance();
        String creationDate = createDateString(now);

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        int currentId = pref.getInt(NOTE_ID, -1);


        Log.v(TAG, noteTitle);
        Log.v(TAG, noteBody);
        Log.v(TAG, creationDate);
        Log.v(TAG, ""+currentId);

        //Handles starting note id and note file creation
        FileOutputStream file = null;
        if (currentId == -1) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(NOTE_ID, 0);
            editor.apply();


            /*try {
                file = openFileOutput(getString(R.string.note_file), MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
        }
        else {
            /*try {
                file = openFileOutput(getString(R.string.note_file), MODE_APPEND)
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
        }
        try {
            file = openFileOutput(getString(R.string.note_file), MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JSONObject note = new JSONObject();
        try {
            note.put(NOTE_ID, currentId);
            note.put(NOTE_TITLE, noteTitle);
            note.put(NOTE_BODY, noteBody);
            note.put(NOTE_CREATED, creationDate);
            note.put(NOTE_EDITED, creationDate);

            pref.edit()
                    .putInt(NOTE_ID, currentId+1)
                    .apply();

            file.write(note.toString().getBytes());
            file.close();
        }
        catch (JSONException e) {
            Log.e(TAG, "JSON error writing note");
        }
        catch (IOException e) {
            Log.e(TAG, "Error writing to note file");
        }


    }

    public static String createDateString(Calendar c) {
        return c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " +
                c.get(Calendar.DAY_OF_MONTH) + ", " +
                c.get(Calendar.YEAR) + " " +
                c.get(Calendar.HOUR) + ":" +
                c.get(Calendar.MINUTE) + ":" +
                c.get(Calendar.SECOND) + ":" +
                c.get(Calendar.MILLISECOND) + " " +
                c.getDisplayName(Calendar.AM_PM, Calendar.LONG, Locale.ENGLISH);
    }
}
