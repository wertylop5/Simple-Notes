package com.projectsling.simplenotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.FileObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.projectsling.simplenotes.util.JsonString;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String NOTE_LIST_TAG = "noteList";
    private static final int CREATE_NOTE_REQUEST = 0;

    private Button mCreateButton;
    private ListView mNoteListView;
    private List<JSONObject> mNotes;
    private NoteAdapter mNoteAdapter;


    private class ReadNoteFileTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            BufferedReader file = null;
            try {
                file = new BufferedReader(
                        new InputStreamReader(openFileInput(getString(R.string.note_file))));
                Log.v(TAG, file.toString());

                //print all notes in file
                JSONObject note;
                String line;
                while((line = file.readLine()) != null) {
                    note = new JSONObject(line);
                    Log.v(TAG, note.toString(4));
                    mNotes.add(note);
                }

            } catch (FileNotFoundException e) {
                Log.e(TAG, "FileNotFoundException", e);
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException", e);
            }
            finally {
                try {
                    if (file != null) {
                        file.close();
                        Log.v(TAG, "File closed");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException", e);
                }
            }
            return null;
        }
    }

    private class NoteAddListener extends FileObserver {
        public NoteAddListener (String path) {
            super(path, FileObserver.MODIFY);
        }

        @Override
        public void onEvent(int event, String path) {
            if (event != FileObserver.MODIFY) {
                Log.wtf(TAG, "File not modified");
            }
            else {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mNotes = new ArrayList<>();

            new ReadNoteFileTask().execute();
        }
        else {
            Log.v(TAG, "onCreate: restore bundle");
            mNotes = JsonString.stringListToJson(savedInstanceState.getStringArrayList(NOTE_LIST_TAG));
        }

        //for (int x = 0; x < 30; x++) mNotes.add(null);

        //mNotes.add(null);

        mCreateButton = (Button) findViewById(R.id.createNoteButton);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateActivity.class);

                startActivityForResult(intent, CREATE_NOTE_REQUEST);
            }
        });

        mNoteListView = (ListView) findViewById(R.id.noteList);

        mNoteAdapter = new NoteAdapter(this, mNotes);
        mNoteListView.setAdapter(mNoteAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_NOTE_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    //CreateActivity should return a json object in string form
                    mNotes.add(
                            new JSONObject(
                                    data.getStringExtra(getString(R.string.intent_note_list_tag))));
                    /*for (int x = 0; x < mNoteAdapter.getCount(); x++) {
                        Log.v(TAG, mNoteAdapter.getItem(x).toString());
                    }*/

                    mNoteAdapter.notifyDataSetChanged();
                }
                catch (JSONException e) {
                    Log.e(TAG, "", e);
                }

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mNotes == null) {
            Log.v(TAG, "onRestore");
            mNotes = JsonString.stringListToJson(savedInstanceState.getStringArrayList(NOTE_LIST_TAG));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(NOTE_LIST_TAG, JsonString.jsonListToString(mNotes));
    }
}
