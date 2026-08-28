package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.hub;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class HubBuildingRegistry {
    private final Map<String, List<BuildingBlueprint>> blueprints = new HashMap<>();

    public void register(String id, int levelToReach, String name, Map<String, Integer> costs) {
        blueprints.computeIfAbsent(id, k -> new ArrayList<>()).add(new BuildingBlueprint(name, costs, levelToReach));
    }

    public BuildingBlueprint getBlueprintForNextLevel(String id, Team team) {
        int currentLevel = team.getHubUpgradeLevel(id);
        int nextLevel = currentLevel + 1;
        
        List<BuildingBlueprint> list = blueprints.get(id);
        if (list != null) {
            for (BuildingBlueprint bp : list) {
                if (bp.getTargetLevel() == nextLevel) {
                    return bp;
                }
            }
        }
        return null; // Pas d'amelioration superieure
    }
}