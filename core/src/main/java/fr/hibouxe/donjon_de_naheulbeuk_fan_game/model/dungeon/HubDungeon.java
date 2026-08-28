package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.DungeonGenerator.Room;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EventResult;
import java.util.ArrayList;

public class HubDungeon extends Dungeon {

    public HubDungeon() {
        super(30, 30);
        this.setDrawWalls(false);
    }

    @Override
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character getRandomMonster(int floorNumber, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.IRandomProvider random, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository repository) {
        return null;
    }

    @Override
    public boolean isExpeditionComplete(int floorNumber) {
        return false;
    }

    @Override
    public java.util.List<String> getFloorIntroDialogues(int floorNumber) {
        java.util.List<String> list = new ArrayList<>();
        list.add("HUB_INTRO");
        return list;
    }

    @Override
    public boolean prepareFloor(int floorNumber, Team team, IMonsterRepository repository) {
        this.setWidth(30);
        this.setHeight(30);
        this.setGrid(new Cell[30][30]);

        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 30; y++) {
                this.getGrid()[x][y] = new Cell(x, y);
                this.getGrid()[x][y].setWall(true);
            }
        }

        // Zone eclaircie
        for (int x = 5; x < 25; x++) {
            for (int y = 3; y < 27; y++) {
                this.getGrid()[x][y].setWall(false);
                this.getGrid()[x][y].setRoomId(1);
            }
        }
        
        this.setPrefabRooms(new ArrayList<>());

        // Point de depart du joueur (Sud, Z=25 car Z=30 est le bas)
        team.setX(15);
        team.setY(25);
        team.setPlayerX(15f);
        team.setPlayerZ(25f);

        // ENTREE DU DONJON (Nord) [15][5]
        this.getGrid()[15][5].setEvent(t -> {
            EventResult res = new EventResult(true);
            res.setActionTrigger("ENTER_DUNGEON");
            return res;
        });
        this.getGrid()[15][5].setStairs(true);

        // FEU CENTRAL / STATUE (Centre) [15][15]
        this.getGrid()[15][15].setEvent(t -> {
            EventResult res = new EventResult(true);
            res.setActionTrigger("OPEN_TAVERN"); // Sert pour sauvegarder/se reposer
            return res;
        });
        this.getGrid()[15][15].setStairs(true);

        // TABLEAU DE QUETES (Fixe, entre l'entree et la taverne) [12][10]
        this.getGrid()[12][10].setEvent(t -> {
            EventResult res = new EventResult(true);
            res.setActionTrigger("OPEN_QUEST_BOARD");
            return res;
        });
        this.getGrid()[12][10].setStairs(true);
        // Ajout visuel du tableau
        Room boardRoom = new Room(11, 9, 3, 3);
        boardRoom.prefabModel = "models/dungeon/naheulbeuk/wall.obj"; // Placeholder
        this.getPrefabRooms().add(boardRoom);

        // 1. TAVERNE / BIVOUAC (Nord-Ouest) [10][8]
        int tavernLevel = team.getHubUpgradeLevel("TAVERNE");
        if (tavernLevel == 0) {
            // Pas de modele, espace vide pour le tuto
            this.getGrid()[10][8].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_TAVERNE");
                return res;
            });
            this.getGrid()[10][8].setStairs(true);
        } else if (tavernLevel == 1) {
            Room tentRoom = new Room(8, 8, 5, 5);
            tentRoom.prefabModel = "models/dungeon/naheulbeuk/rooms/spawn_room_3x3.obj"; // Tente
            this.getPrefabRooms().add(tentRoom);
            
            this.getGrid()[10][8].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_TAVERNE");
                return res;
            });
            this.getGrid()[10][8].setStairs(true);
        } else {
            Room tavernRoom = new Room(8, 8, 5, 5);
            tavernRoom.prefabModel = "models/dungeon/naheulbeuk/rooms/test_room_5x5.obj"; // Taverne
            this.getPrefabRooms().add(tavernRoom);
            
            this.getGrid()[10][8].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("ENTER_TAVERNE");
                return res;
            });
            this.getGrid()[10][8].setStairs(true);
        }

        // 2. MARCHAND (Nord-Est) [20][8]
        int merchantLevel = team.getHubUpgradeLevel("MARCHAND");
        if (merchantLevel == 0) {
            Room cartRoom = new Room(19, 8, 3, 3);
            cartRoom.prefabModel = "models/dungeon/naheulbeuk/wall.obj"; // Chariot casse
            this.getPrefabRooms().add(cartRoom);

            this.getGrid()[20][8].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_MARCHAND");
                return res;
            });
            this.getGrid()[20][8].setStairs(true);
        } else {
            Room marchantRoom = new Room(19, 8, 3, 3);
            marchantRoom.prefabModel = "models/dungeon/naheulbeuk/rooms/spawn_room_3x3.obj"; // Bazar
            this.getPrefabRooms().add(marchantRoom);
            
            this.getGrid()[20][8].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("OPEN_SHOP");
                return res;
            });
            this.getGrid()[20][8].setStairs(true);
        }

        // 3. BANQUE / COMPTOIR DES NAINS (Sud-Est) [22][22]
        int bankLevel = team.getHubUpgradeLevel("BANQUE");
        if (bankLevel == 0) {
            Room chestRoom = new Room(20, 22, 5, 5);
            chestRoom.prefabModel = "models/dungeon/naheulbeuk/rooms/spawn_room_3x3.obj"; // Coffre
            this.getPrefabRooms().add(chestRoom);

            this.getGrid()[22][22].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_BANQUE");
                return res;
            });
            this.getGrid()[22][22].setStairs(true);
        } else {
            Room bankRoom = new Room(20, 22, 5, 5);
            bankRoom.prefabModel = "models/dungeon/naheulbeuk/rooms/test_room_5x5.obj"; // Comptoir
            this.getPrefabRooms().add(bankRoom);
            
            this.getGrid()[22][22].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("OPEN_BANK");
                return res;
            });
            this.getGrid()[22][22].setStairs(true);
        }

        // 4. FORGE D'ARGENT (Sud-Ouest) [8][22]
        int forgeLevel = team.getHubUpgradeLevel("FORGE");
        if (forgeLevel == 0) {
            Room boxesRoom = new Room(6, 22, 5, 5);
            boxesRoom.prefabModel = "models/dungeon/naheulbeuk/wall.obj"; // Caisses
            this.getPrefabRooms().add(boxesRoom);

            this.getGrid()[8][22].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_FORGE");
                return res;
            });
            this.getGrid()[8][22].setStairs(true);
        } else {
            Room forgeRoom = new Room(6, 22, 5, 5);
            forgeRoom.prefabModel = "models/dungeon/naheulbeuk/rooms/test_room_5x5.obj"; // Forge
            this.getPrefabRooms().add(forgeRoom);
            
            this.getGrid()[8][22].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("OPEN_FORGE");
                return res;
            });
            this.getGrid()[8][22].setStairs(true);
        }

        return true;
    }
}