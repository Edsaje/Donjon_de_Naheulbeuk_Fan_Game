package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Moteur d'Affichage Graphique 2.5D (HD-2D).
 * Implémente {@link IGameView} pour remplacer la console ASCII par un rendu 3D + Sprites 2D.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class GraphicHD2DView implements IGameView {
    private HD2DCamera camera = new HD2DCamera();
    private List<BillboardSprite> activeSprites = new ArrayList<>();

    @Override
    public void displayMessage(String message) {
        System.out.println("[HD-2D UI] " + message);
    }

    @Override
    public int askPlayerInt() {
        return 0; // Sera raccordé aux boutons graphiques
    }

    @Override
    public String askPlayerString() {
        return "";
    }

    // --- ÉCRAN TITRE ET MENU PRINCIPAL GRAPHISÉS ---

    @Override
    public void displayTitleScreen() {
        System.out.println("[Rendu HD-2D] Écran-Titre 3D avec caméra panoramique et particules !");
    }

    @Override
    public int askMainMenuChoice() {
        return 1;
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
        return 1;
    }

    @Override
    public int askSlotManagementAction() {
        return 0;
    }

    @Override
    public int askTargetCopySlot(int sourceSlot, String[] slotSummaries) {
        return 0;
    }

    // --- RENDU HD-2D DU DONJON ET DÉPLACEMENT ---

    @Override
    public void display(Dungeon maze, Team team) {
        displayDungeon(maze, team);
    }

    @Override
    public void displayDungeon(Dungeon maze, Team team) {
        // 1. Mise à jour de la Caméra 3D inclinée à -45° sur la position de la Compagnie
        camera.updateTarget(team);

        // 2. Vider et régénérer la liste des Sprites 2D Billboards présents sur la carte
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

        // 3. Rendu d'information du Moteur Graphique HD-2D
        System.out.println("\n[Moteur HD-2D] --- RENDU IMAGE 3D ---");
        System.out.println("🎥 Caméra 3D : Position(" + camera.getCameraX() + ", " + camera.getCameraY() + ", " + camera.getCameraZ() + ") | Pitch(" + camera.getPitch() + "°)");
        System.out.println("🎯 Cible Caméra : (" + camera.getTargetX() + ", 0.0, " + camera.getTargetZ() + ")");
        System.out.println("🕺 Sprites 2D Billboards affichés debout face caméra : " + activeSprites.size() + " entités");
    }

    @Override
    public String askPlayerMovement() {
        return "Z"; // Sera raccordé aux touches clavier / manette (Z,S,Q,D / Flèches)
    }

    @Override
    public boolean askPickupItem(Item item) {
        return true;
    }

    // --- INVENTAIRE ET COMBAT GRAPHISÉS ---

    @Override
    public void displayInventory(Team team) {
    }

    @Override
    public int askInventoryMenuChoice() {
        return 0;
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
        return 0;
    }

    @Override
    public Character askItemTarget(Team team) {
        return null;
    }

    @Override
    public void displayBattleStatus(List<Character> monsters, Team team) {
        System.out.println("[Rendu HD-2D] Combat 3D déclenché avec animations de compétences !");
    }

    @Override
    public int askBattleAction(Character attacker) {
        return 1;
    }

    @Override
    public Skill askSkill(Character attacker, List<Character> monsters) {
        return null;
    }

    @Override
    public Character askMonsterTarget(List<Character> monsters) {
        return null;
    }

    @Override
    public Character askAllyToHeal(Team team) {
        return null;
    }

    @Override
    public void displayTeamStats(Team team) {
    }
}
