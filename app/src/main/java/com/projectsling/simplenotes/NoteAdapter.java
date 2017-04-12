package com.projectsling.simplenotes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Stanley on 4/12/2017.
 */
public class NoteAdapter extends BaseAdapter {
    private Context mContext;
    private List<JSONObject> mNotes;

    public NoteAdapter(Context c, List<JSONObject> l) {
        mContext = c;
        mNotes = l;
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return null;
    }
}
