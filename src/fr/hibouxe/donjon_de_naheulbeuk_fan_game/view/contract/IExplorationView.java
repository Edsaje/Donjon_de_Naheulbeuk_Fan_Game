package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;

public interface IExplorationView {
    void displayTransitionScreen(int floorNumber);
    void display(Dungeon maze, Team team, int currentFloor);
    void displayDungeon(Dungeon maze, Team team, int currentFloor);
    String askPlayerMovement();
    boolean askPickupItem(Item item);
    void displaySaveSuccess(int slot);
    void displaySaveError();
}
