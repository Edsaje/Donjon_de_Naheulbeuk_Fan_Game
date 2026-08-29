package fr.hibouxe.donjon_de_naheulbeuk_fan_game.infrastructure.save;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Contrleur d'E/S Fichier pour le systme de sauvegarde Multi-Slots (Slots 1, 2, 3).
 * Gre la liaison entre Sauvegarde Rapide (quicksave_slotX.sav) et Sauvegarde Permanente (savegame_slotX.sav),
 * ainsi que la suppression et la copie d'emplacements de profil.
 *
 * @author Hibouxe
 * @version 2.0
 */
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.ISaveManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.SaveData;


public class FileSaveManager implements ISaveManager {

    /**
     * Obtenir le nom du fichier de Sauvegarde Rapide pour un slot donn.
     */
    public String getQuickSaveFilename(int slot) {
        return "quicksave_slot" + slot + ".sav";
    }

    /**
     * Obtenir le nom du fichier de Sauvegarde Permanente pour un slot donn.
     */
    public String getHubSaveFilename(int slot) {
        return "savegame_slot" + slot + ".sav";
    }

    /**
     * Effectue une Sauvegarde Rapide lie au slot actif.
     */
    public boolean saveQuickSave(int slot, Team team, Dungeon dungeon, int currentFloor) {
        SaveData data = new SaveData(team, dungeon, currentFloor, true);
        return saveToFile(data, getQuickSaveFilename(slot));
    }

    /**
     * Effectue une Sauvegarde Permanente au Campement lie au slot actif.
     */
    public boolean saveHubSave(int slot, Team team, int currentFloor) {
        SaveData data = new SaveData(team, null, currentFloor, false);
        return saveToFile(data, getHubSaveFilename(slot));
    }

    /**
     * Charge la Sauvegarde Rapide du slot spcifi.
     */
    public SaveData loadQuickSave(int slot) {
        return loadFromFile(getQuickSaveFilename(slot));
    }

    /**
     * Charge la Sauvegarde du Campement du slot spcifi.
     */
    public SaveData loadHubSave(int slot) {
        return loadFromFile(getHubSaveFilename(slot));
    }

    /**
     * Vrifie si une Sauvegarde Rapide existe pour ce slot.
     */
    public boolean hasQuickSave(int slot) {
        File f = new File(getQuickSaveFilename(slot));
        return f.exists() && f.length() > 0;
    }

    /**
     * Vrifie si une Sauvegarde Permanente de campement existe pour ce slot.
     */
    public boolean hasHubSave(int slot) {
        File f = new File(getHubSaveFilename(slot));
        return f.exists() && f.length() > 0;
    }

    /**
     * Vrifie si le slot contient au moins une sauvegarde (quicksave ou hubsave).
     */
    public boolean hasAnySave(int slot) {
        return hasQuickSave(slot) || hasHubSave(slot);
    }

    /**
     * Supprime la Sauvegarde Rapide d'un slot spcifique.
     */
    public boolean deleteQuickSave(int slot) {
        File f = new File(getQuickSaveFilename(slot));
        if (f.exists()) {
            return f.delete();
        }
        return false;
    }

    /**
     * Supprime dfinitivement toutes les donnes (quicksave et hubsave) d'un slot.
     */
    public boolean deleteSlot(int slot) {
        boolean delQuick = deleteQuickSave(slot);
        File hubFile = new File(getHubSaveFilename(slot));
        boolean delHub = false;
        if (hubFile.exists()) {
            delHub = hubFile.delete();
        }
        return delQuick || delHub;
    }

    /**
     * Duplique un emplacement de profil (Slot source vers Slot cible).
     */
    public boolean copySlot(int sourceSlot, int targetSlot) {
        if (sourceSlot == targetSlot) return false;

        deleteSlot(targetSlot);
        boolean copied = false;

        if (hasQuickSave(sourceSlot)) {
            SaveData data = loadQuickSave(sourceSlot);
            if (data != null) {
                saveToFile(data, getQuickSaveFilename(targetSlot));
                copied = true;
            }
        }

        if (hasHubSave(sourceSlot)) {
            SaveData data = loadHubSave(sourceSlot);
            if (data != null) {
                saveToFile(data, getHubSaveFilename(targetSlot));
                copied = true;
            }
        }

        return copied;
    }

    /**
     * Gnre un rsum lisible du contenu d'un emplacement de profil.
     *
     * @param slot Le numro du slot (1, 2 ou 3)
     * @return Rsum formrat (ex: "[Slot 1] Compagnie Niv.3 | tage 4 (En Donjon)")
     */
    public String getSlotSummary(int slot) {
        if (hasQuickSave(slot)) {
            SaveData data = loadQuickSave(slot);
            if (data != null && data.getTeam() != null) {
                int level = getTeamMaxLevel(data.getTeam());
                return "[Slot " + slot + "] Compagnie Niv." + level + " | tage " + data.getCurrentFloor() + " (Sauvegarde Rapide en Donjon)";
            }
        }

        if (hasHubSave(slot)) {
            SaveData data = loadHubSave(slot);
            if (data != null && data.getTeam() != null) {
                int level = getTeamMaxLevel(data.getTeam());
                return "[Slot " + slot + "] Compagnie Niv." + level + " | Au Campement (Sauvegarde Permanente)";
            }
        }

        return "[Slot " + slot + "] Emplacement Vide";
    }

    private int getTeamMaxLevel(Team team) {
        int maxLvl = 1;
        if (team.getMembers() != null) {
            for (Character c : team.getMembers()) {
                if (c.getLevel() > maxLvl) {
                    maxLvl = c.getLevel();
                }
            }
        }
        return maxLvl;
    }

    private boolean saveToFile(SaveData data, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
            return true;
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("FileSaveManager", "Erreur lors de la sauvegarde de " + filename, e);
            return false;
        }
    }

    private SaveData loadFromFile(String filename) {
        File f = new File(filename);
        if (!f.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (SaveData) ois.readObject();
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("FileSaveManager", "Erreur lors du chargement de " + filename, e);
            return null;
        }
    }
}
