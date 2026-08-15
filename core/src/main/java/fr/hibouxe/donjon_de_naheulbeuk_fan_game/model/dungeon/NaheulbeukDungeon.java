package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import java.util.List;

public class NaheulbeukDungeon extends Dungeon {

    public NaheulbeukDungeon() {
        super(21, 21); // Taille de base (qui pourra s'étendre)
    }

    @Override
    public boolean prepareFloor(int floorNumber, Team team) {
        // 1. Adapter la taille selon l'étage
        int newSize = 21 + (floorNumber * 2); // De plus en plus grand !
        this.setWidth(newSize);
        this.setHeight(newSize);
        this.setGrid(new Cell[newSize][newSize]);
        for (int x = 0; x < newSize; x++) {
            for (int y = 0; y < newSize; y++) {
                this.getGrid()[x][y] = new Cell(x, y);
            }
        }

        // 2. Générer la structure
        generatePMDDungeon();

        // 3. Placer l'équipe au point de départ
        int[] startPos = getFirstWalkablePosition();
        team.setX(startPos[0]);
        team.setY(startPos[1]);

        // 4. Peuplement et Scénario spécifique à Naheulbeuk
        if (floorNumber == 4) {
            
            generateMonsters(8, team.getX(), team.getY());
            generateItems(3);
            generateStairs(1);
        } else if (floorNumber == 5) {
            
            
            
            
            
            // Boss spawn at startPos (or maybe near startPos?) Let's put it on the current start position, or the team's position.
            // Actually, the ExplorationController checked if the team killed the boss on the *stairs* previously.
            // Wait, in the old logic, when currentFloor == 5, they spawned the boss on the currentCell when stepping on stairs. 
            // In prepareFloor, we spawn the boss on the floor. Let's spawn it in the room.
            generateMonsters(0, team.getX(), team.getY()); // Just to clean up
            
            // On spawne le boss à la première place libre
            int[] bossPos = getFirstWalkablePosition(); // Usually same as start pos but we'll let them walk into it
            List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> bossList = new java.util.ArrayList<>();
            bossList.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem());
            this.getGrid()[bossPos[0]][bossPos[1]].setMonsters(bossList);
            // Pas d'escalier, car c'est la fin !
        } else {
            // étages standards
            generateMonsters(5 + floorNumber, team.getX(), team.getY());
            generateItems(3);
            generateStairs(1);
        }
        
        return false; // L'expédition n'est pas encore terminée
    }

    @Override
    public boolean isExpeditionComplete(int floorNumber) {
        if (floorNumber == 5) {
            // Check if Golem is dead. It's the only monster left.
            // Wait, if we return true from here, how do we know the Golem is dead?
            // ExplorationController clears the monster from the cell on victory.
            // We can just iterate over all cells and check if any has a Golem.
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    if (getGrid()[x][y].hasMonster()) {
                        for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character c : getGrid()[x][y].getMonsters()) {
                            if (c instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem) {
                                return false; // Golem is still alive
                            }
                        }
                    }
                }
            }
            return true; // Golem is dead!
        }
        return false;
    }
}
