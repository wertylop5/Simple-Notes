package com.projectsling.simplenotes.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanley on 4/14/2017.
 */
public class JsonString {
    private static final String TAG = JsonString.class.getSimpleName();

    //For use with bundles
    public static ArrayList<String> jsonListToString(List<JSONObject> l) {
        ArrayList<String> res = new ArrayList<>();

        for (JSONObject j : l) {
            res.add(j.toString());
        }

        return res;
    }

    //For use with bundles
    public static ArrayList<JSONObject> stringListToJson(List<String> l) {
        ArrayList<JSONObject> res = new ArrayList<>();

        for (String s : l) {
            try {
                res.add(new JSONObject(s));
            } catch (JSONException e) {
                Log.e(TAG, "JSONException", e);
            }
        }

        return res;
    }
}
