package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.List;

/**
 * Moteur d'Affichage Graphique 2.5D (HD-2D).
 * ImplÃ©mente {@link IGameView} pour remplacer la console ASCII par un rendu 3D + Sprites 2D.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class GraphicHD2DView implements IGameView {
    private HD2DCamera camera = new HD2DCamera();
    private List<BillboardSprite> activeSprites = new ArrayList<>();
    private HD2DGameApp gameApp;
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();

    public GraphicHD2DView() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Donjon de Naheulbeuk - Fan Game (Rendu 3D HD-2D)");
        config.setWindowedMode(1280, 720);
        config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);
        config.setForegroundFPS(60);

        gameApp = new HD2DGameApp(this);
        new Thread(() -> {
            new Lwjgl3Application(gameApp, config);
        }).start();
    }

    private List<String> messageHistory = new ArrayList<>();

    @Override
    public void clearMessages() {
        messageHistory.clear();
        if (gameApp != null) {
            gameApp.setMessages(new ArrayList<>(messageHistory));
        }
    }

    @Override
    public void displayMessage(String message) {
        String[] lines = message.split("\n");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                messageHistory.add(line);
            }
        }
        if (messageHistory.size() > 8) {
            messageHistory.subList(0, messageHistory.size() - 8).clear();
        }
        if (gameApp != null) {
            gameApp.setMessages(new ArrayList<>(messageHistory));
        }
    }

    @Override
    public void displayDialogue(String message) {
        displayMessage(message);
        // Force the app to pause and wait for the player to press a key
        inputQueue.clear();
        if (gameApp != null) {
            // Un "MenuRequest" vide mais avec 1 option invisible pour forcer la pause
            gameApp.setMenuRequest(null, new String[]{"[Continuer]"});
        }
        try {
            inputQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (gameApp != null) {
                gameApp.setMenuRequest(null, null);
            }
        }
    }

    public void pushInput(String input) {
        inputQueue.offer(input);
    }

    @Override
    public int askPlayerInt() {
        if (gameApp != null) {
            gameApp.setState(HD2DGameApp.GameState.HUB);
            gameApp.setMenuRequest("CONFIRMATION", new String[]{"Oui / Confirmer", "Non / Annuler"});
        }
        try {
            int choice = Integer.parseInt(inputQueue.take());
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return choice;
        } catch (Exception e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return 2;
        }
    }

    @Override
    public String askPlayerString() {
        return "";
    }

    // --- Ã‰CRAN TITRE ET ConsoleMenu PRINCIPAL GRAPHISÃ‰S ---

    @Override
    public void displayTitleScreen() {
        // Le rendu de l'écran titre est gÃ©rÃà par LibGDX, pas besoin de console
    }

    @Override
    public int askMainMenuChoice() {
        if (gameApp != null) {
            gameApp.setState(HD2DGameApp.GameState.HUB); // Reusing HubController state for generic menus
            gameApp.setMenuRequest("ConsoleMenu PRINCIPAL", new String[]{"Nouvelle Partie", "Charger Partie", "Gérer Sauvegardes", "Quitter"});
        }
        
        try {
            int choice = Integer.parseInt(inputQueue.take());
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return choice;
        } catch (InterruptedException | NumberFormatException e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return 4; // Default to Quitter
        }
    }

    @Override
    public int askHubChoice() {
        if (gameApp != null) {
            gameApp.setState(HD2DGameApp.GameState.HUB);
            gameApp.setMenuRequest("CAMPEMENT", new String[]{"Donjon", "Compagnie", "Inventaire", "Sauvegarder", "Quitter"});
        }
        
        try {
            int choice = Integer.parseInt(inputQueue.take());
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return choice;
        } catch (InterruptedException | NumberFormatException e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return 1;
        }
    }

    @Override
    public boolean askLoadQuickSavePrompt() {
        return true;
    }

    @Override
    public boolean askLoadQuickSavePrompt(int slot, String summary) {
        return true;
    }

    @Override
    public boolean askConfirmAbandonQuickSave() {
        return false;
    }

    // --- GESTION DES EMPLACEMENTS (SLOTS) ---

    @Override
    public int askSlotChoice(String actionTitle, String[] slotSummaries) {
        inputQueue.clear();
        if (gameApp != null) {
            gameApp.setState(HD2DGameApp.GameState.HUB);
            
            // Generate options like "Slot 1: summary", "Slot 2: summary", etc.
            String[] options = new String[slotSummaries.length + 1];
            for (int i = 0; i < slotSummaries.length; i++) {
                options[i] = "Slot " + (i + 1) + ": " + slotSummaries[i];
            }
            options[slotSummaries.length] = "Retour";
            
            gameApp.setMenuRequest(actionTitle, options);
        }
        
        try {
            int choice = Integer.parseInt(inputQueue.take());
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            if (choice > slotSummaries.length) return 0; // Return 0 for 'Retour' as expected by Game.java
            return choice;
        } catch (Exception e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return 0;
        }
    }

    @Override
    public int askSlotManagementAction() {
        if (gameApp != null) {
            gameApp.setState(HD2DGameApp.GameState.HUB);
            gameApp.setMenuRequest("GESTION SAUVEGARDES", new String[]{"Copier", "Supprimer", "Retour"});
        }
        try {
            int choice = Integer.parseInt(inputQueue.take());
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            if (choice == 3) return 0;
            return choice;
        } catch (Exception e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return 0;
        }
    }

    @Override
    public int askTargetCopySlot(int sourceSlot, String[] slotSummaries) {
        return askSlotChoice("COPIER VERS...", slotSummaries);
    }

    // --- RENDU HD-2D DU DONJON ET DÃ‰PLACEMENT ---

    @Override
    public void displayTransitionScreen(int floorNumber) {
        if (gameApp != null) {
            gameApp.setState(HD2DGameApp.GameState.TRANSITION);
            gameApp.setTransitionFloor(floorNumber);
        }
    }

    @Override
    public void display(Dungeon maze, Team team, int currentFloor) {
        displayDungeon(maze, team, currentFloor);
    }

    @Override
    public void displayDungeon(Dungeon maze, Team team, int currentFloor) {
        if (gameApp != null) {
            gameApp.setState(HD2DGameApp.GameState.EXPLORATION);
            gameApp.setContext(maze, team, currentFloor);
        }
        
        // 1. Mise à jour de la CamÃ©ra 3D inclinÃ©e à -45à sur la position de la Compagnie
        camera.updateTarget(team);

        // 2. Vider et rÃ©gÃ©nÃ©rer la liste des Sprites 2D Billboards prÃ©sents sur la carte
        activeSprites.clear();

        // Sprite 2D de la Compagnie
        if (team.getMembers() != null && !team.getMembers().isEmpty()) {
            BillboardSprite teamSprite = new BillboardSprite(team.getMembers().get(0), team.getX(), team.getY());
            teamSprite.faceCamera(camera.getCameraX(), camera.getCameraZ());
            activeSprites.add(teamSprite);
        }

        // Sprites 2D des monstres visibles dans le donjon
        for (int x = 0; x < maze.getWidth(); x++) {
            for (int y = 0; y < maze.getHeight(); y++) {
                if (maze.getGrid()[x][y].hasMonster()) {
                    for (Character monster : maze.getGrid()[x][y].getMonsters()) {
                        BillboardSprite monsterSprite = new BillboardSprite(monster, x, y);
                        monsterSprite.faceCamera(camera.getCameraX(), camera.getCameraZ());
                        activeSprites.add(monsterSprite);
                    }
                }
            }
        }
    }

    @Override
    public String askPlayerMovement() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean askPickupItem(Item item) {
        return true;
    }

    // --- INVENTAIRE ET COMBAT GRAPHISÃ‰S ---

    private Team lastTeamForInventory = null;

    @Override
    public void displayInventory(Team team) {
        this.lastTeamForInventory = team;
        // L'inventaire est gÃ©rÃà et affichÃà visuellement via le HUDRenderer (LibGDX)
    }

    @Override
    public int askInventoryMenuChoice() {
        if (gameApp != null) {
            String[] options = {"Utiliser/équiper", "Déséquiper", "Fermer"};
            gameApp.setMenuRequest("SAC À DOS", options);
        }
        try {
            int choice = Integer.parseInt(inputQueue.take());
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return choice;
        } catch (Exception e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return 3;
        }
    }

    @Override
    public EquipmentSlot askSlotToUnequip() {
        return null;
    }

    @Override
    public boolean askUseItem() {
        return false;
    }

    @Override
    public int askItemIndex() {
        if (gameApp != null && lastTeamForInventory != null) {
            String[] options = new String[lastTeamForInventory.getInventory().size() + 1];
            for (int i = 0; i < lastTeamForInventory.getInventory().size(); i++) {
                options[i] = lastTeamForInventory.getInventory().get(i).getName();
            }
            options[lastTeamForInventory.getInventory().size()] = "Annuler";
            gameApp.setMenuRequest("CHOIX OBJET", options);
        }
        try {
            int choice = Integer.parseInt(inputQueue.take()) - 1;
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return choice;
        } catch (Exception e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return -1;
        }
    }

    @Override
    public Character askItemTarget(Team team) {
        inputQueue.clear();
        String[] options = new String[team.getMembers().size() + 1];
        for (int i = 0; i < team.getMembers().size(); i++) {
            options[i] = team.getMembers().get(i).getName() + " (" + team.getMembers().get(i).getHealthPoint() + " PV)";
        }
        options[team.getMembers().size()] = "Annuler";

        if (gameApp != null) {
            gameApp.setMenuRequest("CIBLE ALLIÃ‰E", options);
        }

        try {
            int choice = Integer.parseInt(inputQueue.take());
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            if (choice > 0 && choice <= team.getMembers().size()) return team.getMembers().get(choice - 1);
        } catch (Exception e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
        }
        return null;
    }

    @Override
    public void displayBattleStatus(List<Character> monsters, Team team) {
        if (gameApp != null) {
            gameApp.setState(HD2DGameApp.GameState.BATTLE);
            gameApp.setupBattle(team, monsters);
        }
    }

    @Override
    public int askBattleAction(Character attacker) {
        inputQueue.clear();
        if (gameApp != null) {
            String[] options = {"Attaquer", "CompÃ©tence", "Inventaire", "Fuir"};
            gameApp.setMenuRequest("ACTIONS: " + attacker.getName(), options);
        }
        try {
            int choice = Integer.parseInt(inputQueue.take());
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return choice;
        } catch (Exception e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            return 1;
        }
    }

    @Override
    public Skill askSkill(Character attacker, List<Character> monsters) {
        inputQueue.clear();
        List<Skill> skills = attacker.getSkills();
        String[] options = new String[skills.size() + 1];
        for (int i = 0; i < skills.size(); i++) {
            options[i] = skills.get(i).getName();
        }
        options[skills.size()] = "Annuler";
        
        if (gameApp != null) {
            gameApp.setMenuRequest("COMPÃ‰TENCES", options);
        }
        
        try {
            int choice = Integer.parseInt(inputQueue.take());
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            if (choice > 0 && choice <= skills.size()) return skills.get(choice - 1);
        } catch (Exception e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
        }
        return null;
    }
    @Override
    public Character askMonsterTarget(List<Character> monsters) {
        inputQueue.clear();
        List<Character> aliveMonsters = new ArrayList<>();
        for (Character m : monsters) {
            if (m.getHealthPoint() > 0) {
                aliveMonsters.add(m);
            }
        }
        
        String[] options = new String[aliveMonsters.size() + 1];
        for (int i = 0; i < aliveMonsters.size(); i++) {
            options[i] = aliveMonsters.get(i).getName() + " (" + aliveMonsters.get(i).getHealthPoint() + " PV)";
        }
        options[aliveMonsters.size()] = "Annuler";

        if (gameApp != null) {
            gameApp.setMenuRequest("CIBLE ENNEMIE", options);
        }

        try {
            int choice = Integer.parseInt(inputQueue.take());
            if (gameApp != null) gameApp.setMenuRequest(null, null);
            if (choice > 0 && choice <= aliveMonsters.size()) return aliveMonsters.get(choice - 1);
        } catch (Exception e) {
            if (gameApp != null) gameApp.setMenuRequest(null, null);
        }
        return null;
    }

    @Override
    public void displayTeamStats(Team team) {
    }

    @Override
    public void displayTurn(String characterName) {
        displayMessage("\n⚡ C'est au tour de " + characterName + " !");
    }

    @Override
    public void displayVictory() {
        displayMessage("\n C'est trop facile..");
    }

    @Override
    public void displayDefeat() {
        displayMessage("\n Plutôt paradis des Nains ou des Aventuriers ?");
    }

    @Override
    public void displaySaveSuccess(int slot) {
        displayMessage("\n[Sauvegarde Rapide] Donjon et position enregistrés dans le Slot " + slot + ". Retour à l'écran initial...");
    }

    @Override
    public void displaySaveError() {
        displayMessage("\n[Erreur] Échec de la Sauvegarde Rapide.");
    }
}