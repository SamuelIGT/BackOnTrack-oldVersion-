package br.ufc.quixada.backontrack.model;

import java.util.List;

/**
 * Created by samue on 25/07/2017.
 */

public class Level {

    private int level;
    private List<Section> sectionsList;

    public Level(int level, List<Section>sectionsList){
        this.level = level;
        this.sectionsList = sectionsList;
    }

    public List<Section> getSectionsList() {
        return sectionsList;
    }

    public void removeSec(int sectionID) {
        Section sec = find_sec(sectionID);
        if(sec != null){
            sectionsList.remove(sec);
        }
    }

    public void addSec(Section sec){
        if(sec != null) {
            if (sec.getExerciseList() != null) {
                sectionsList.add(sec);
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private Section find_sec(int id){
        for(Section sec: sectionsList) {
            if(sec.getID() == id){
                return sec;
            }
        }
        return null;
    }



}
