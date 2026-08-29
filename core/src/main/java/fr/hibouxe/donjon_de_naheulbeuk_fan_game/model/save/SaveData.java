package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

import java.io.Serializable;

/**
 * Conteneur de donnes pour la srialisation des sauvegardes (QuickSave Donjon et Save Normale HubController).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Team team;
    private Dungeon dungeon;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.hub.Village village;
    private int currentFloor;
    private boolean isQuickSave;

    /**
     * Construit un objet de donnes de sauvegarde.
     *
     * @param team         La compagnie de hros
     * @param dungeon      Le labyrinthe du donjon (null si sauvegarde au HubController)
     * @param currentFloor L'tage actuel
     * @param isQuickSave  true si c'est une QuickSave temporaire de donjon, false si c'est une sauvegarde du HubController
     */
    public SaveData(Team team, Dungeon dungeon, int currentFloor, boolean isQuickSave) {
        this.team = team;
        this.dungeon = dungeon;
        this.village = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.hub.Village(); // Fallback for now
        this.currentFloor = currentFloor;
        this.isQuickSave = isQuickSave;
    }

    public Team getTeam() {
        return team;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.hub.Village getVillage() {
        if (village == null) village = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.hub.Village(); // For old saves compatibility
        return village;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public boolean isQuickSave() {
        return isQuickSave;
    }
}
