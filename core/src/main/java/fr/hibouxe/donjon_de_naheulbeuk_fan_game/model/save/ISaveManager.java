package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

public interface ISaveManager {
    boolean saveQuickSave(int slot, Team team, Dungeon dungeon, int currentFloor);
    boolean saveHubSave(int slot, Team team, int currentFloor);
    SaveData loadQuickSave(int slot);
    SaveData loadHubSave(int slot);
    boolean hasQuickSave(int slot);
    boolean hasHubSave(int slot);
    boolean hasAnySave(int slot);
    boolean deleteQuickSave(int slot);
    boolean deleteSlot(int slot);
    boolean copySlot(int sourceSlot, int targetSlot);
    String getSlotSummary(int slot);
}
