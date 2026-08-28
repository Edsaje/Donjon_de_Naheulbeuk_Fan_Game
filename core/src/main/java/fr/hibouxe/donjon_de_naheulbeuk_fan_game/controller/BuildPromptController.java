package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;

public class BuildPromptController implements GameState {
    private Team team;
    private IMenuView menu;
    private GameContext gameContext;
    private String buildingId;
    private int costGold;
    private String buildingName;
    private Runnable onBuildSuccess;

    public BuildPromptController(Team team, IMenuView menu, GameContext gameContext, String buildingId, String buildingName, int costGold, Runnable onBuildSuccess) {
        this.team = team;
        this.menu = menu;
        this.gameContext = gameContext;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.costGold = costGold;
        this.onBuildSuccess = onBuildSuccess;
    }

    @Override
    public void enter() {
        menu.setMenuRequest("Construire " + buildingName + " ?", new String[]{"Oui (" + costGold + " Or)", "Non"});
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
                if (team.getGold() >= costGold) {
                    team.setGold(team.getGold() - costGold);
                    team.setHubUpgradeLevel(buildingId, 1);
                    menu.displayMessage("Vous avez construit : " + buildingName + " !");
                    if (onBuildSuccess != null) onBuildSuccess.run();
                } else {
                    menu.displayMessage("Pas assez d'or ! Il vous manque " + (costGold - team.getGold()) + " Or.");
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