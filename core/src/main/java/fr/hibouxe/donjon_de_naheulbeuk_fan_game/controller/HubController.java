package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.ISaveManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;

public class HubController implements GameState {
    private Team team;
    private IMenuView menu;
    private int activeSlot = 1;
    private ISaveManager saveManager;
    private GameContext gameContext;

    public HubController(Team team, IMenuView menu, int activeSlot, ISaveManager saveManager, GameContext gameContext) {
        this.team = team;
        this.menu = menu;
        this.activeSlot = activeSlot;
        this.saveManager = saveManager;
        this.gameContext = gameContext;
    }

    @Override
    public void enter() {
        menu.setMenuRequest("La Taverne", new String[]{"Se Reposer (Sauvegarder)", "Retour au Campement", "Quitter le jeu"});
    }

    @Override
    public void update(float deltaTime) {}

    @Override
    public void onInput(String action) {
        if ("ENTER".equals(action)) {
            int choice = menu.getMenuSelection();
            menu.resetMenuSelection();
            switch (choice) {
                case 0:
                    for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character c : team.getMembers()) {
                        c.setHealthPoint(c.getMaxHealthPoint());
                        c.setCurrentResource(c.getMaxResource());
                    }
                    boolean saved = saveManager.saveHubSave(activeSlot, team, 1);
                    if (saved) {
                        menu.displayMessage("L'equipe est reposee. Progression enregistree (Slot " + activeSlot + ") !");
                    } else {
                        menu.displayMessage("Echec de la sauvegarde.");
                    }
                    menu.setMenuRequest("La Taverne", new String[]{"Se Reposer (Sauvegarder)", "Retour au Campement", "Quitter le jeu"});
                    break;
                case 1:
                    menu.setMenuRequest(null, null);
                    if (gameContext != null) gameContext.popState();
                    break;
                case 2:
                    com.badlogic.gdx.Gdx.app.exit();
                    break;
            }
        }
    }

    @Override
    public void exit() {}
}