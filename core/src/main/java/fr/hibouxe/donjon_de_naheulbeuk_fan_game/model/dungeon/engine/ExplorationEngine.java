package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.engine;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EventResult;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.engine.MoveResult;

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

    public MoveResult processPlayerMove(int deltaX, int deltaY) {
        int targetX = team.getX() + deltaX;
        int targetY = team.getY() + deltaY;

        if (targetX >= 0 && targetX < maze.getWidth() && targetY >= 0 && targetY < maze.getHeight()) {
            Cell targetCell = maze.getGrid()[targetX][targetY];

            if (targetCell.hasBlockingEvent()) {
                EventResult result = targetCell.getEvent().trigger(team);
                return new MoveResult(MoveResult.MoveStatus.EVENT_TRIGGERED, result);
            }

            if (targetCell.isWalkable()) {
                team.move(deltaX, deltaY);

                return new MoveResult(MoveResult.MoveStatus.SUCCESS);
            }
        }
        return new MoveResult(MoveResult.MoveStatus.BLOCKED);
    }
}

