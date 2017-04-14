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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity {
    private final String TAG = CreateActivity.class.getSimpleName();

    //private final String NOTE_ID        = getString(R.string.note_id);
    //private final String NOTE_TITLE     = getString(R.string.note_title);
    //private final String NOTE_BODY      = getString(R.string.note_body);
    //private final String NOTE_CREATED   = getString(R.string.note_created);
    //private final String NOTE_EDITED    = getString(R.string.note_edited);


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

        //For the id counter
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        int currentId = pref.getInt(getString(R.string.note_id), -1);

        Log.v(TAG, noteTitle);
        Log.v(TAG, noteBody);
        Log.v(TAG, creationDate);
        Log.v(TAG, ""+currentId);

        //Handles starting note id and note file creation
        PrintWriter file = null;
        if (currentId == -1) {
            //Note id counter is just a simple key value pair
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(getString(R.string.note_id), 0);
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
            file = new PrintWriter(openFileOutput(getString(R.string.note_file), MODE_APPEND));
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException", e);
        }

        JSONObject note = new JSONObject();
        try {
            note.put(getString(R.string.note_id), currentId);
            note.put(getString(R.string.note_title), noteTitle);
            note.put(getString(R.string.note_body), noteBody);
            note.put(getString(R.string.note_created), creationDate);
            note.put(getString(R.string.note_edited), creationDate);

            pref.edit()
                    .putInt(getString(R.string.note_id), currentId+1)
                    .apply();

            file.println(note.toString());
            file.close();

            Intent intent = new Intent();
            intent.putExtra(getString(R.string.intent_note_list_tag), note.toString());
            setResult(RESULT_OK, intent);
        }
        catch (JSONException e) {
            Log.e(TAG, "JSON error writing note");
            setResult(RESULT_CANCELED);
        }

        finish();
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
