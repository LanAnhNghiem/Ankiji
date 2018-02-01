package com.jishin.ankiji.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jishin.ankiji.model.Moji;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by lana on 01/02/2018.
 */

public class MojiConverter {
    @TypeConverter
    public static String toMojiStr(ArrayList<Moji> list){
        Gson gsonBuilder = new GsonBuilder().create();
        // Convert Java ArrayList into JSON
        String jsonString = gsonBuilder.toJson(list);
        return jsonString;
    }
    @TypeConverter
    public static ArrayList<Moji> toArrayList(String string){
        ArrayList<Moji> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for(int i=0; i<jsonArray.length(); i++) {
                Moji word = new Moji();
                word.setId(jsonArray.getJSONObject(i).getString("id"));
                word.setCachDocHira(jsonArray.getJSONObject(i).getString("cachDocHira"));
                word.setAmHan(jsonArray.getJSONObject(i).getString("amHan"));
                word.setTuTiengNhat(jsonArray.getJSONObject(i).getString("tuTiengNhat"));
                word.setNghiaTiengViet(jsonArray.getJSONObject(i).getString("nghiaTiengViet"));
                list.add(word);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
