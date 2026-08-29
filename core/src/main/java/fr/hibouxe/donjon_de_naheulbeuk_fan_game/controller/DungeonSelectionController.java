package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;

public class DungeonSelectionController implements GameState {
    private IMenuView menu;
    private GameContext gameContext;

    public DungeonSelectionController(IMenuView menu, GameContext gameContext) {
        this.menu = menu;
        this.gameContext = gameContext;
    }

    @Override
    public void enter() {
        menu.setMenuRequest("SELECTION_DONJON", new String[]{
            "Tutoriel (Cave du Tavernier)", 
            "Donjon de Naheulbeuk", 
            "Fort de Schlipak", 
            "Ruines", 
            "Annuler"
        });
    }

    @Override
    public void update(float deltaTime) {}

    @Override
    public void onInput(String action) {
        if ("ENTER".equals(action)) {
            int choice = menu.getMenuSelection();
            menu.resetMenuSelection();
            
            if (gameContext == null) return;
            
            switch (choice) {
                case 0:
                    gameContext.startDungeon("TUTORIAL");
                    break;
                case 1:
                    gameContext.startDungeon("NAHEULBEUK");
                    break;
                case 2:
                    menu.displayMessage("La Foret n'est pas encore disponible.");
                    menu.setMenuRequest(null, null);
                    gameContext.popState();
                    break;
                case 3:
                    menu.displayMessage("La Grotte n'est pas encore disponible.");
                    menu.setMenuRequest(null, null);
                    gameContext.popState();
                    break;
                case 4:
                case -1:
                    menu.setMenuRequest(null, null);
                    gameContext.popState();
                    break;
            }
        } else if ("ECHAP".equals(action) || "X".equals(action)) {
            menu.resetMenuSelection();
            menu.setMenuRequest(null, null);
            if (gameContext != null) gameContext.popState();
        }
    }

    @Override
    public void exit() {}
}