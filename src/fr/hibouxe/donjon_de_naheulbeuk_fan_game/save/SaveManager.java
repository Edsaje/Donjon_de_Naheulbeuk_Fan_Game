package fr.hibouxe.donjon_de_naheulbeuk_fan_game.save;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Contrôleur d'E/S Fichier pour la sauvegarde et le chargement de parties (QuickSave et HubSave).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class SaveManager {
    public static final String QUICK_SAVE_FILE = "quicksave.sav";
    public static final String HUB_SAVE_FILE = "savegame.sav";

    /**
     * Effectue une Sauvegarde Rapide (QuickSave) au sein du donjon.
     *
     * @param team         L'équipe avec sa position (X, Y)
     * @param dungeon      Le donjon avec la carte des cases
     * @param currentFloor L'étage actuel
     * @return true si la sauvegarde a réussi, false sinon.
     */
    public static boolean saveQuickSave(Team team, Dungeon dungeon, int currentFloor) {
        SaveData data = new SaveData(team, dungeon, currentFloor, true);
        return saveToFile(data, QUICK_SAVE_FILE);
    }

    /**
     * Effectue une Sauvegarde Permanente au Campement (Hub).
     *
     * @param team         L'équipe avec son sac et ses statistiques
     * @param currentFloor L'étage maximum atteint
     * @return true si la sauvegarde a réussi, false sinon.
     */
    public static boolean saveHubSave(Team team, int currentFloor) {
        SaveData data = new SaveData(team, null, currentFloor, false);
        return saveToFile(data, HUB_SAVE_FILE);
    }

    /**
     * Charge les données de la Sauvegarde Rapide.
     *
     * @return Les données SaveData ou null si indisponible.
     */
    public static SaveData loadQuickSave() {
        return loadFromFile(QUICK_SAVE_FILE);
    }

    /**
     * Charge les données de la Sauvegarde du Hub.
     *
     * @return Les données SaveData ou null si indisponible.
     */
    public static SaveData loadHubSave() {
        return loadFromFile(HUB_SAVE_FILE);
    }

    /**
     * Vérifie si une Sauvegarde Rapide existe.
     *
     * @return true si le fichier quicksave.sav est présent
     */
    public static boolean hasQuickSave() {
        File f = new File(QUICK_SAVE_FILE);
        return f.exists() && f.length() > 0;
    }

    /**
     * Vérifie si une Sauvegarde Permanente existe.
     *
     * @return true si le fichier savegame.sav est présent
     */
    public static boolean hasHubSave() {
        File f = new File(HUB_SAVE_FILE);
        return f.exists() && f.length() > 0;
    }

    /**
     * Supprime la Sauvegarde Rapide (lors d'une défaite ou du retour au Hub).
     *
     * @return true si supprimée
     */
    public static boolean deleteQuickSave() {
        File f = new File(QUICK_SAVE_FILE);
        if (f.exists()) {
            return f.delete();
        }
        return false;
    }

    private static boolean saveToFile(SaveData data, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static SaveData loadFromFile(String filename) {
        File f = new File(filename);
        if (!f.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (SaveData) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
