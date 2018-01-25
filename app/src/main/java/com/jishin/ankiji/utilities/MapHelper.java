package com.jishin.ankiji.utilities;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jishin.ankiji.model.Kanji;
import com.jishin.ankiji.model.Moji;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.model.User;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by lana on 24/01/2018.
 */

public class MapHelper {
    public static final String TAG = MapHelper.class.getSimpleName();
    // access a map node via path // path style: "string/to/path"
    public static Map getPath(Map map, String path){
        String[] subPaths = path.split("/");

        Map result = map;
        for (String subPath:
                subPaths) {
            result = (Map) result.get(subPath);
        }
        return result;
    }
    public static ArrayList<Set> convertToSet(Map<String, Map> map){
        ArrayList<Set> list = new ArrayList<>();
        Gson gson = new Gson();
        for (Map.Entry<String, Map> entry : map.entrySet())
        {
            Gson gsonBuilder = new GsonBuilder().create();
            // Convert Java Map into JSON
            String jsonStr = gsonBuilder.toJson(entry.getValue());
            Log.d(TAG +"json",jsonStr );
            Set set = gson.fromJson(jsonStr, Set.class);
            list.add(set);
        }
        return list;
    }
    public static ArrayList<Moji> convertToMoji(Map<String, Map> map){
        ArrayList<Moji> list = new ArrayList<>();
        Gson gson = new Gson();
        for (Map.Entry<String, Map> entry : map.entrySet())
        {
            Gson gsonBuilder = new GsonBuilder().create();
            // Convert Java Map into JSON
            String jsonStr = gsonBuilder.toJson(entry.getValue())
                    .replace("[","").replace("]","");
            Log.d(TAG +"json",jsonStr );
            Moji set = gson.fromJson(jsonStr, Moji.class);
            list.add(set);
        }
        return list;
    }
    public static ArrayList<Kanji> convertToKanji(Map<String, Map> map){
        ArrayList<Kanji> list = new ArrayList<>();
        Gson gson = new Gson();
        for (Map.Entry<String, Map> entry : map.entrySet())
        {
            Gson gsonBuilder = new GsonBuilder().create();
            // Convert Java Map into JSON
            String jsonStr = gsonBuilder.toJson(entry.getValue()).replace("[","").replace("]","");
            Log.d(TAG +"json",jsonStr );
            Kanji set = gson.fromJson(jsonStr, Kanji.class);
            list.add(set);
        }
        return list;
    }
    public static ArrayList<User> convertToUser(Map<String, Map> map){
        ArrayList<User> list = new ArrayList<>();
        Gson gson = new Gson();
        for (Map.Entry<String, Map> entry : map.entrySet())
        {
            Gson gsonBuilder = new GsonBuilder().create();
            // Convert Java Map into JSON
            String jsonStr = gsonBuilder.toJson(entry.getValue());
            Log.d(TAG +"json",jsonStr );
            User set = gson.fromJson(jsonStr, User.class);
            list.add(set);
        }
        return list;
    }
}
