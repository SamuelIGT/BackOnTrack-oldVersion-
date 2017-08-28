package br.ufc.quixada.backontrack;

/**
 * Created by samuel on 25/08/17.
 */

public class ExerciseReport {
    private int exerciseId;
    private String exerciseTitle;
    private int effort;
    private long dateMillis;
    private String time;

    public ExerciseReport(int exerciseId, String exerciseTitle, int effort, long dateMillis, String time) {
        this.exerciseId = exerciseId;
        this.exerciseTitle = exerciseTitle;
        this.effort = effort;
        this.dateMillis = dateMillis;
        this.time = time;
    }

    public ExerciseReport(int exerciseId) {
        this.exerciseId = exerciseId;
    }


    public int getExerciseId() {
        return exerciseId;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public int getEffort() {
        return effort;
    }

    public long getDateMillis() {
        return dateMillis;
    }

    public String getTime() {
        return time;
    }

    public void setExerciseTitle(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }

    public void setDateMillis(long dateMillis) {
        this.dateMillis = dateMillis;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
