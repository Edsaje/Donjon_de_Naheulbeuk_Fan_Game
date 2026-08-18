package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Blueprint for generating a dungeon floor in a data-driven way.
 */
public class FloorBlueprint implements Serializable {
    private static final long serialVersionUID = 1L;

    private int size;
    private int numMonsters;
    private int numItems;
    private int numStairs;
    private List<String> introDialogues = new ArrayList<>();
    private String bossType; // Null if no boss

    public FloorBlueprint() {
    }

    public FloorBlueprint(int size, int numMonsters, int numItems, int numStairs) {
        this.size = size;
        this.numMonsters = numMonsters;
        this.numItems = numItems;
        this.numStairs = numStairs;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumMonsters() {
        return numMonsters;
    }

    public void setNumMonsters(int numMonsters) {
        this.numMonsters = numMonsters;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public int getNumStairs() {
        return numStairs;
    }

    public void setNumStairs(int numStairs) {
        this.numStairs = numStairs;
    }

    public List<String> getIntroDialogues() {
        return introDialogues;
    }

    public void setIntroDialogues(List<String> introDialogues) {
        this.introDialogues = introDialogues;
    }

    public String getBossType() {
        return bossType;
    }

    public void setBossType(String bossType) {
        this.bossType = bossType;
    }
}
