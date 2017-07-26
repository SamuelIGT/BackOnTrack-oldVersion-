package br.ufc.quixada.backontrack.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by samue on 25/07/2017.
 */

public class Section implements Serializable{
    private int id;
    private String title;
    private List<Exercise> exerciseList;

    public Section(int id, String title, List<Exercise> exerciseList) {
        this.id = id;
        this.title = title;
        this.exerciseList = exerciseList;
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
}
