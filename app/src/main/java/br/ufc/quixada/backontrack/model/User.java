package br.ufc.quixada.backontrack.model;

/**
 * Created by samue on 07/08/2017.
 */

public class User {

    private int levelPermission;

    public User(int levelPermission) {
        this.levelPermission = levelPermission;
    }

    public int getLevelPermission() {
        return levelPermission;
    }

    public void setLevelPermission(int levelPermission) {
        this.levelPermission = levelPermission;
    }
}
