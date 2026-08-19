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

    @Override
    public void update(float deltaTime) {
        // Input polling (continu) handled here or by InputManager
    }

    @Override
    public void onInput(String action) {
        if ("UP".equals(action) || "Z".equals(action)) {
            playerZ -= moveSpeed * 0.1f;
        } else if ("DOWN".equals(action) || "S".equals(action)) {
            playerZ += moveSpeed * 0.1f;
        } else if ("LEFT".equals(action) || "Q".equals(action)) {
            playerX -= moveSpeed * 0.1f;
        } else if ("RIGHT".equals(action) || "D".equals(action)) {
            playerX += moveSpeed * 0.1f;
        }
        
        if ("ENTER".equals(action)) {
            handleInteraction();
        }
        
        if ("M".equals(action) || "ESCAPE".equals(action)) {
            openVillageMenu();
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

