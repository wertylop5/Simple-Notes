package com.projectsling.simplenotes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Stanley on 4/12/2017.
 */
public class NoteAdapter extends BaseAdapter {
    private static final String TAG = NoteAdapter.class.getSimpleName();

    private Context mContext;
    private List<JSONObject> mNotes;

    public NoteAdapter(Context c, List<JSONObject> l) {
        mContext = c;
        mNotes = l;
    }

    private class ViewHolder {
        public TextView mTitleText;
        public TextView mBodyText;
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

        ViewHolder holder;
        if (convertView == null) {
            //equivalent to context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            LayoutInflater inflater = LayoutInflater.from(mContext);

            convertView = inflater.inflate(R.layout.row_note_adapter, parent, false);
            holder = new ViewHolder();
            holder.mTitleText = (TextView)convertView.findViewById(R.id.noteTitle);
            holder.mBodyText = (TextView)convertView.findViewById(R.id.noteBody);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        try {
            holder.mTitleText.setText(
                    mNotes.get(position).getString(mContext.getString(R.string.note_title))
            );

            holder.mBodyText.setText(
                    mNotes.get(position).getString(mContext.getString(R.string.note_body))
            );
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }


        return convertView;
    }
}
