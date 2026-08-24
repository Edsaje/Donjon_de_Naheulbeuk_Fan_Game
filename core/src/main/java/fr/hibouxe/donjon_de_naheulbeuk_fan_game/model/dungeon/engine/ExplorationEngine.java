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
        // Initialize team floating pos if needed
        this.team.setPlayerX(team.getX() + 0.5f);
        this.team.setPlayerZ(team.getY() + 0.5f);
    }

    public void setDungeon(Dungeon maze) {
        this.maze = maze;
    }

    /**
     * Updates physics for continuous movement.
     * @return true if the integer grid coordinates changed
     */
    public boolean updatePhysics(float deltaTime, boolean up, boolean down, boolean left, boolean right) {
        float moveSpeed = team.getMoveSpeed();
        float playerX = team.getPlayerX();
        float playerZ = team.getPlayerZ();
        
        float nextX = playerX;
        float nextZ = playerZ;
        
        if (up) nextZ -= moveSpeed * deltaTime;
        if (down) nextZ += moveSpeed * deltaTime;
        if (left) nextX -= moveSpeed * deltaTime;
        if (right) nextX += moveSpeed * deltaTime;
        
        float radius = 0.25f; // Logical radius of the player's hitbox
        
        // Move X
        float boundedNextX = Math.max(radius, Math.min(nextX, maze.getWidth() - radius));
        int minZ = (int) (playerZ - radius);
        int maxZ = (int) (playerZ + radius);
        boolean canMoveX = true;
        if (boundedNextX > playerX) {
            int hitX = (int)(boundedNextX + radius);
            if (!maze.getGrid()[hitX][minZ].isWalkable() || !maze.getGrid()[hitX][maxZ].isWalkable()) canMoveX = false;
        } else if (boundedNextX < playerX) {
            int hitX = (int)(boundedNextX - radius);
            if (!maze.getGrid()[hitX][minZ].isWalkable() || !maze.getGrid()[hitX][maxZ].isWalkable()) canMoveX = false;
        }
        
        int cx = (int)boundedNextX; int cz = (int)playerZ;
        Cell currentCellX = maze.getGrid()[cx][cz];
        if (currentCellX.hasDoor()) {
            boolean isDoorNS = false;
            if (cx > 0 && maze.getGrid()[cx-1][cz].isWall()) isDoorNS = true;
            else if (cx < maze.getWidth() - 1 && maze.getGrid()[cx+1][cz].isWall()) isDoorNS = true;
            
            if (isBlockedByDoor(boundedNextX, playerZ, currentCellX, isDoorNS)) canMoveX = false;
        }

        int oldGridX = (int) playerX;
        if (canMoveX) playerX = boundedNextX;
        
        // Move Z
        float boundedNextZ = Math.max(radius, Math.min(nextZ, maze.getHeight() - radius));
        int minX = (int) (playerX - radius);
        int maxX = (int) (playerX + radius);
        boolean canMoveZ = true;
        if (boundedNextZ > playerZ) {
            int hitZ = (int)(boundedNextZ + radius);
            if (!maze.getGrid()[minX][hitZ].isWalkable() || !maze.getGrid()[maxX][hitZ].isWalkable()) canMoveZ = false;
        } else if (boundedNextZ < playerZ) {
            int hitZ = (int)(boundedNextZ - radius);
            if (!maze.getGrid()[minX][hitZ].isWalkable() || !maze.getGrid()[maxX][hitZ].isWalkable()) canMoveZ = false;
        }
        
        cx = (int)playerX; cz = (int)boundedNextZ;
        Cell currentCellZ = maze.getGrid()[cx][cz];
        if (currentCellZ.hasDoor()) {
            boolean isDoorNS = false;
            if (cx > 0 && maze.getGrid()[cx-1][cz].isWall()) isDoorNS = true;
            else if (cx < maze.getWidth() - 1 && maze.getGrid()[cx+1][cz].isWall()) isDoorNS = true;
            
            if (isBlockedByDoor(playerX, boundedNextZ, currentCellZ, isDoorNS)) canMoveZ = false;
        }

        int oldGridZ = (int) playerZ;
        if (canMoveZ) playerZ = boundedNextZ;

        if (up) team.setFacingDirection(1);
        else if (down) team.setFacingDirection(0);
        else if (left) team.setFacingDirection(2);
        else if (right) team.setFacingDirection(3);

        int newGridX = (int) playerX;
        int newGridZ = (int) playerZ;
        
        team.setPlayerX(playerX);
        team.setPlayerZ(playerZ);
        
        return (newGridX != oldGridX || newGridZ != oldGridZ);
    }

    private boolean isBlockedByDoor(float newX, float newZ, Cell cell, boolean isNS) {
        if (cell.isDoorOpen()) return false;
        
        float lx = newX - (int)newX;
        float lz = newZ - (int)newZ;
        
        if (isNS) {
            if (lz > 0.35f && lz < 0.65f) {
                if (!cell.isDoorOpen()) return true; 
                if (lx < 0.35f || lx > 0.65f) return true; 
            }
        } else {
            if (lx > 0.35f && lx < 0.65f) {
                if (!cell.isDoorOpen()) return true; 
                if (lz < 0.35f || lz > 0.65f) return true; 
            }
        }
        return false;
    }
}
