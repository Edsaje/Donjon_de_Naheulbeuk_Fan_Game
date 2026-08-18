package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.engine;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;

public class ExplorationEngine {
    private Dungeon maze;
    private Team team;

    public ExplorationEngine(Dungeon maze, Team team) {
        this.maze = maze;
        this.team = team;
    }

    public void setDungeon(Dungeon maze) {
        this.maze = maze;
    }

    public boolean processPlayerMove(int deltaX, int deltaY) {
        int targetX = team.getX() + deltaX;
        int targetY = team.getY() + deltaY;

        if (targetX >= 0 && targetX < maze.getWidth() && targetY >= 0 && targetY < maze.getHeight()) {
            Cell targetCell = maze.getGrid()[targetX][targetY];

            if (targetCell.hasBlockingEvent()) {
                return false;
            }

            if (targetCell.isWalkable()) {
                team.move(deltaX, deltaY);
                
                if (!targetCell.hasMonster() && !targetCell.hasStairs()) {
                    maze.moveMonsters(team);
                }
                return true;
            }
        }
        return false;
    }
}
