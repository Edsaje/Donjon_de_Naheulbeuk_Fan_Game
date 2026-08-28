package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.hub;

import java.util.Map;

public class BuildingBlueprint {
    private String name;
    private Map<String, Integer> costs;
    private int targetLevel;

    public BuildingBlueprint(String name, Map<String, Integer> costs, int targetLevel) {
        this.name = name;
        this.costs = costs;
        this.targetLevel = targetLevel;
    }

    public String getName() { return name; }
    public Map<String, Integer> getCosts() { return costs; }
    public int getTargetLevel() { return targetLevel; }
}