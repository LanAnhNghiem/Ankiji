package com.jishin.ankiji.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jishin.ankiji.model.Kanji;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by lana on 01/02/2018.
 */

public class KanjiConverter {
    @TypeConverter
    public static String toArrayList(ArrayList<Kanji> list){
        Gson gsonBuilder = new GsonBuilder().create();
        // Convert Java ArrayList into JSON
        String jsonString = gsonBuilder.toJson(list);
        return jsonString;
    }
    @TypeConverter
    public static ArrayList<Kanji> toKanjiString(String string){
        ArrayList<Kanji> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for(int i=0; i<jsonArray.length(); i++) {
                Kanji word = new Kanji();
                word.setId(jsonArray.getJSONObject(i).getString("id"));
                word.setAmhan(jsonArray.getJSONObject(i).getString("amhan"));
                word.setTuvung(jsonArray.getJSONObject(i).getString("tuvung"));
                word.setKanji(jsonArray.getJSONObject(i).getString("kanji"));
                list.add(word);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
