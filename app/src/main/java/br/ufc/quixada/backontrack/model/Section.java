package br.ufc.quixada.backontrack.model;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import java.io.Serializable;
import java.util.List;

/**
 * Created by samue on 25/07/2017.
 */

public class Section implements Serializable{
    private int id;
    private String title;
    private List<Exercise> exerciseList;
    private boolean isUnlocked = false;

    public Section(int id, String title, List<Exercise> exerciseList, boolean isUnlocked) {
        this.id = id;
        this.title = title;
        this.exerciseList = exerciseList;
        this.isUnlocked = isUnlocked;
    }

    public int getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public void addExerc(Exercise Exerc){
        if(Exerc != null) {
            exerciseList.add(Exerc);
        }
    }

    public void removeExerc(int exercID) {
        Exercise exerc = findExerc(exercID);
        if (exerc != null){
            exerciseList.remove(exerc);
        }
    }

    private Exercise findExerc(int exercID){
        for (Exercise exerc: exerciseList){
            if(exerc.getId() == exercID){
                return exerc;
            }
        }
        return null;
    }

    public boolean isSectionCompleted(){
        for(Exercise exercise: exerciseList){
            if (!exercise.getStatus().equals("DONE")){//SHOULD BE GET STATUS
                return false;
            }
        }
        return true;
    }

    public void updateSection(Section sec){
        this.exerciseList = sec.getExerciseList();
        this.isUnlocked = sec.isUnlocked();
    }
}
