package br.ufc.quixada.backontrack;

import android.content.Context;
import android.util.Log;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import br.ufc.quixada.backontrack.model.Level;

import br.ufc.quixada.backontrack.model.User;

/**
 * Created by samuel on 23/08/17.
 */

public class StorageController {
    private StorageService storage;
    private Gson jsonParser;

    public StorageController() {
        storage = new StorageService();
        jsonParser = new GsonBuilder().create();
    }

    public ArrayList<Event> getCalendar(String key, Context context){
        String calendarSerialized;
        calendarSerialized = storage.getDefaults(key, context);
        if (calendarSerialized != null){
            Type eventsListType = new TypeToken<List<Event>>(){}.getType();
            return jsonParser.fromJson(calendarSerialized, eventsListType);
        }
        Log.v("DEBUG_STORAGE_GET", context.getString(R.string.DEBUG_STORAGE_GET));

        return null;
    }

    public void saveCalendar(String key, List<Event> calendarEvents, Context context){

        String calendarSerialized;
        calendarSerialized = jsonParser.toJson(calendarEvents);

        storage.setDefaults(key, calendarSerialized, context);
    }

    public void saveLevels(String key, List<Level> levels, Context context){
        String levelsSerialized;
        levelsSerialized = jsonParser.toJson(levels);

        storage.setDefaults(key, levelsSerialized, context);
    }

    public List<Level> getLevels(String key, Context context){
        String levelsSerialized = storage.getDefaults(key, context);

        Type listType = new TypeToken<List<Level>>(){}.getType();

        return jsonParser.fromJson(levelsSerialized, listType);
    }

    public User getUser(String key, Context context){
        String userSerialized = storage.getDefaults(key, context);

        return jsonParser.fromJson(userSerialized, User.class);
    }

    public void saveUser(String key, User user, Context context){
        String userSerialized;
        userSerialized = jsonParser.toJson(user);

        storage.setDefaults(key, userSerialized, context);
    }


    /*public void saveReport(String key, ExerciseReport report, Context context){
        String reportSerialized;
        reportSerialized = jsonParser.toJson(report);

        storage.setDefaults(key, reportSerialized, context);
    }

    public ExerciseReport getReport(){

    }*/


}
