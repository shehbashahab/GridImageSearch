package com.codepath.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shehba.shahab on 9/22/15.
 */
public class ImageResult implements Serializable {

    private static final long serialVersionUID = -2893089570992474768L;
    public String fullUrl;
    public String thumbUrl;

    private ImageResult(JSONObject json) {

        try {
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
        ArrayList<ImageResult> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return results;
    }
}
