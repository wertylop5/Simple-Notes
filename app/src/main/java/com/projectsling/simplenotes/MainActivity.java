package com.projectsling.simplenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    private Button mCreateButton;
    private ListView mNoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCreateButton = (Button) findViewById(R.id.createNoteButton);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateActivity.class);
                startActivity(intent);
            }
        });

        mNoteList = (ListView) findViewById(R.id.noteList);
        //mNoteList.setAdapter(new NoteAdapter());
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            FileInputStream file = openFileInput(getString(R.string.note_file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
