package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.hub.Village;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.ISaveManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;

public class VillageController implements GameState {
    
    private final Team team;
    private final Village village;
    private final IMenuView menu;
    private final ISaveManager saveManager;
    private final int currentSlot;
    private final Game game;
    
    // Deplacement Libre (Free Movement)
    private float playerX = 0f;
    private float playerZ = 0f;
    private float moveSpeed = 6.0f; // Vitesse de deplacement

    public VillageController(Team team, Village village, IMenuView menu, int currentSlot, ISaveManager saveManager, Game game) {
        this.team = team;
        this.village = village;
        this.menu = menu;
        this.currentSlot = currentSlot;
        this.saveManager = saveManager;
        this.game = game;
    }

    @Override
    public void enter() {
        playerX = 0f;
        playerZ = 0f;
        menu.displayMessage("Bienvenue au Campement !");
    }

    private float boundX = 10.0f;
    private float boundZ = 10.0f;

    @Override
    public void update(float deltaTime) {
        if (currentMenuState != VillageMenuState.NONE) return;

        fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.input.IInputProvider input = game.getInputProvider();
        if (input != null) {
            float nextX = playerX;
            float nextZ = playerZ;
            
            if (input.isUpPressed()) nextZ -= moveSpeed * deltaTime;
            if (input.isDownPressed()) nextZ += moveSpeed * deltaTime;
            if (input.isLeftPressed()) nextX -= moveSpeed * deltaTime;
            if (input.isRightPressed()) nextX += moveSpeed * deltaTime;
            
            // Collision (Limites simples)
            if (nextX < -boundX) nextX = -boundX;
            if (nextX > boundX) nextX = boundX;
            if (nextZ > boundZ) nextZ = boundZ; // Mur Sud fermé
            
            // Porte vers le NORD (Donjons) -> Z négatif
            if (nextZ < -boundZ) {
                if (nextX >= -1.5f && nextX <= 1.5f) {
                    // Laisse avancer un peu pour declencher le menu
                    if (nextZ < -boundZ - 1.0f) {
                        nextZ = -boundZ - 1.0f;
                        openDungeonSelection();
                    }
                } else {
                    nextZ = -boundZ;
                }
            }
            
            playerX = nextX;
            playerZ = nextZ;
        }
    }

    private void openDungeonSelection() {
        currentMenuState = VillageMenuState.DUNGEON_SELECTION;
        menu.setMenuRequest("Partir à l'Aventure ?", new String[]{"Donjon du Tutoriel", "Donjon de Naheulbeuk", "Rester ici"});
    }

    private enum VillageMenuState { NONE, DUNGEON_SELECTION, TAVERN, VILLAGE_MENU }
    private VillageMenuState currentMenuState = VillageMenuState.NONE;

    @Override
    public void onInput(String action) {
        if ("ENTER".equals(action)) {
            if (currentMenuState == VillageMenuState.DUNGEON_SELECTION) {
                int choice = menu.getMenuSelection();
                menu.resetMenuSelection();
                currentMenuState = VillageMenuState.NONE;
                menu.setMenuRequest(null, null);
                if (choice == 0) game.startDungeon("TUTORIAL");
                else if (choice == 1) game.startDungeon("NAHEULBEUK");
                else playerZ += 2.0f; // Recule vers le Sud pour ne pas re-declencher
            } else if (currentMenuState == VillageMenuState.NONE) {
                handleInteraction();
            }
        }
        
        if ("M".equals(action) || "ESCAPE".equals(action)) {
            if (currentMenuState == VillageMenuState.NONE) {
                openVillageMenu();
            } else {
                currentMenuState = VillageMenuState.NONE;
                menu.setMenuRequest(null, null);
                playerZ += 2.0f; // Recule vers le Sud pour eviter la boucle
            }
        }
        
        if (action != null && action.startsWith("MENU_")) {
            handleMenuAction(action);
        }
    }

    private void handleMenuAction(String action) {
        // Gerer les retours du menu
    }

    private void handleInteraction() {
        if (playerZ < -2.0f && village.getTavernLevel() > 0) {
             menu.setMenuRequest("La Taverne", new String[]{"Se reposer (20 PO)", "Ecouter les rumeurs", "Quitter"});
        } else {
             menu.displayMessage("Elfe : 'Il est beau ce campement, mais y'a pas de poneys !'");
        }
    }

    private void openVillageMenu() {
        menu.setMenuRequest("Menu du Village", new String[]{"Aller au Donjon", "Sauvegarder", "Fermer"});
    }

    @Override
    public void exit() {
        menu.setMenuRequest(null, null);
    }
    
    public float getPlayerX() { return playerX; }
    public float getPlayerZ() { return playerZ; }
    public Village getVillage() { return village; }
}

