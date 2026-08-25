package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.DungeonGenerator.Room;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.ICellEvent;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EventResult;
import java.util.ArrayList;

public class HubDungeon extends Dungeon {

    public HubDungeon() {
        super(20, 20);
    }

    @Override
    public boolean prepareFloor(int floorNumber, Team team, IMonsterRepository repository) {
        this.setWidth(20);
        this.setHeight(20);
        this.setGrid(new Cell[20][20]);

        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                this.getGrid()[x][y] = new Cell(x, y);
                this.getGrid()[x][y].setWall(true);
            }
        }

        int startX = 5;
        int startY = 5;
        for (int x = startX; x < 15; x++) {
            for (int y = startY; y < 15; y++) {
                this.getGrid()[x][y].setWall(false);
                this.getGrid()[x][y].setRoomId(1);
            }
        }

        java.util.List<Room> prefabs = new ArrayList<>();
        Room campRoom = new Room(startX, startY, 10, 10);
        campRoom.prefabModel = "models/village/Campement_Decor.obj";
        prefabs.add(campRoom);
        this.setPrefabRooms(prefabs);

        this.getGrid()[10][14].setEvent(t -> {
            EventResult res = new EventResult(true);
            res.setActionTrigger("OPEN_TAVERN");
            return res;
        });

        this.getGrid()[10][5].setEvent(t -> {
            EventResult res = new EventResult(true);
            res.setActionTrigger("ENTER_DUNGEON");
            return res;
        });
        this.getGrid()[10][5].setStairs(true);

        team.setX(10);
        team.setY(9);

        return true;
    }

    @Override
    public java.util.List<String> getFloorIntroDialogues(int floorNumber) {
        return new ArrayList<>();
    }

    @Override
    public boolean isExpeditionComplete(int currentFloor) {
        return false;
    }
}