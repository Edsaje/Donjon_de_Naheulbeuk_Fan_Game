package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

import java.io.Serializable;

/**
 * Conteneur de données pour la sérialisation des sauvegardes (QuickSave Donjon et Save Normale HubController).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Team team;
    private Dungeon dungeon;
    private int currentFloor;
    private boolean isQuickSave;

    /**
     * Construit un objet de données de sauvegarde.
     *
     * @param team         La compagnie de héros
     * @param dungeon      Le labyrinthe du donjon (null si sauvegarde au HubController)
     * @param currentFloor L'étage actuel
     * @param isQuickSave  true si c'est une QuickSave temporaire de donjon, false si c'est une sauvegarde du HubController
     */
    public SaveData(Team team, Dungeon dungeon, int currentFloor, boolean isQuickSave) {
        this.team = team;
        this.dungeon = dungeon;
        this.currentFloor = currentFloor;
        this.isQuickSave = isQuickSave;
    }

    public Team getTeam() {
        return team;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public boolean isQuickSave() {
        return isQuickSave;
    }
}
