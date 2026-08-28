package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;
import java.util.Map;
import java.util.HashMap;

public class BuildPromptController implements GameState {
    private Team team;
    private IMenuView menu;
    private GameContext gameContext;
    private String buildingId;
    private String buildingName;
    private Map<String, Integer> costs;
    private Runnable onBuildSuccess;

    public BuildPromptController(Team team, IMenuView menu, GameContext gameContext, String buildingId, String buildingName, Map<String, Integer> costs, Runnable onBuildSuccess) {
        this.team = team;
        this.menu = menu;
        this.gameContext = gameContext;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.costs = costs;
        this.onBuildSuccess = onBuildSuccess;
    }

    private String getCostString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : costs.entrySet()) {
            sb.append(entry.getValue()).append(" ").append(entry.getKey()).append(" ");
        }
        return sb.toString().trim();
    }

    private boolean hasMaterials() {
        for (Map.Entry<String, Integer> req : costs.entrySet()) {
            if (team.getHubChest().getOrDefault(req.getKey(), 0) < req.getValue()) {
                return false;
            }
        }
        return true;
    }

    private void consumeMaterials() {
        for (Map.Entry<String, Integer> req : costs.entrySet()) {
            int current = team.getHubChest().getOrDefault(req.getKey(), 0);
            team.getHubChest().put(req.getKey(), current - req.getValue());
        }
    }

    @Override
    public void enter() {
        menu.setMenuRequest("Construire " + buildingName + " ?", new String[]{"Oui (" + getCostString() + ")", "Non"});
    }

    @Override
    public void update(float deltaTime) {}

    @Override
    public void onInput(String action) {
        if ("ESCAPE".equals(action) || "X".equals(action)) {
            gameContext.popState();
            return;
        }
        if ("ENTER".equals(action)) {
            int choice = menu.getMenuSelection();
            menu.resetMenuSelection();
            if (choice == 0) { // Oui
                if (hasMaterials()) {
                    consumeMaterials();
                    int currentLvl = team.getHubUpgradeLevel(buildingId);
                    team.setHubUpgradeLevel(buildingId, currentLvl + 1);
                    menu.displayMessage("Vous avez construit : " + buildingName + " !");
                    if (onBuildSuccess != null) onBuildSuccess.run();
                } else {
                    menu.displayMessage("Ressources manquantes ! Il vous faut : " + getCostString());
                }
            }
            gameContext.popState();
        }
    }

    @Override
    public void exit() {
        menu.setMenuRequest(null, null);
    }
}