package br.ufc.quixada.backontrack.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by samue on 17/07/2017.
 */

public class Exercise implements Serializable{
    private Integer id;
    private int level;
    private String title;
    private String description;
    private boolean isUnlocked = true;
    private int videoPath;
    private List<Integer> stepsAudio;
    private List<String> stepsText;
    private int thumbnail;

    public Exercise(){

    }

    public Exercise(int level, String title,/* int duracao,*/ String description, int thumbnail, int videoPath , boolean isUnlocked/*, List<Integer> stepsAudio*/){
        this.level = level;
        this.title = title;
        this.description = description;
        /*this.stepsAudio = stepsAudio;*/
        this.thumbnail = thumbnail;
        this.videoPath = videoPath;
        this.isUnlocked = isUnlocked;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getStepsAudio() {
        return stepsAudio;
    }

    public void setStepsAudio(List<Integer> stepsAudio) {
        this.stepsAudio = stepsAudio;
    }

    public List<String> getStepsText() {
        return stepsText;
    }

    public void setStepsText(List<String> stepsText) {
        this.stepsText = stepsText;
    }

    public int getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(int videoPath) {
        this.videoPath = videoPath;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
