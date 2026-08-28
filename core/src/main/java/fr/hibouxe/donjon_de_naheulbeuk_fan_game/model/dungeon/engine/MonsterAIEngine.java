package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.engine;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.RoamingMonsterGroup;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Thief;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Ogre;

import java.util.Iterator;

public class MonsterAIEngine {
    
    // Pool de liste pour eviter l'allocation a chaque frame (Zero-Allocation)
    private final java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EncounterEvent> frameEncounters = new java.util.ArrayList<>(10);

    public java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EncounterEvent> updateAll(float deltaTime, Dungeon maze, Team team) {
        frameEncounters.clear(); // 0 allocation, reutilisation du cache
        float playerX = team.getPlayerX();
        float playerZ = team.getPlayerZ();

        // Calculate team stealth / intimidation
        float noiseRadius = 1.5f; // Base hearing radius
        float visionMultiplier = 1.0f;
        boolean canIntimidate = false;

        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character leader = team.getActiveLeader();
        if (leader != null) {
            if (leader instanceof Thief) {
                // Thief is stealthy
                noiseRadius = 0.5f; 
                visionMultiplier = 0.5f;
            } else if (leader instanceof Ogre) {
                // Ogre is scary but loud
                noiseRadius = 3.0f;
                canIntimidate = true; // Might cause fleeing
            }
        }

        Iterator<RoamingMonsterGroup> it = maze.getRoamingMonsters().iterator();
        while (it.hasNext()) {
            RoamingMonsterGroup mg = it.next();

            if (mg.isBoss()) {
                mg.setState(RoamingMonsterGroup.AIState.IDLE);
            } else if (!mg.isDefeated()) {
                float dx = playerX - mg.getX();
                float dz = playerZ - mg.getZ();
                float dist = (float) Math.sqrt(dx * dx + dz * dz);

                boolean canSee = false;
                boolean canHear = (dist <= noiseRadius);

                if (dist < 6.0f * visionMultiplier) {
                    if (isPlayerInFOV(mg, dx, dz)) {
                        if (hasLineOfSight(maze, mg.getX(), mg.getZ(), playerX, playerZ)) {
                            canSee = true;
                        }
                    }
                }

                switch (mg.getState()) {
                    case SLEEPING:
                        if (canHear || (canSee && dist < 2.0f)) {
                            mg.setState(RoamingMonsterGroup.AIState.ALERT);
                            mg.setAlertTimer(1.0f);
                        }
                        break;
                    case PATROL:
                        if (canSee || canHear) {
                            mg.setState(RoamingMonsterGroup.AIState.ALERT);
                            mg.setAlertTimer(0.5f);
                        } else {
                            mg.setStateTimer(mg.getStateTimer() - deltaTime);
                            if (mg.getStateTimer() <= 0) {
                                mg.setFacingDirection((int)(Math.random() * 4));
                                mg.setStateTimer(1.0f + (float)Math.random() * 2.0f);
                            }
                            moveForward(mg, 0.8f * deltaTime, maze);
                        }
                        break;
                    case ALERT:
                        mg.setAlertTimer(mg.getAlertTimer() - deltaTime);
                        faceTowards(mg, dx, dz);
                        if (mg.getAlertTimer() <= 0) {
                            String mainMonster = mg.getMonsters().isEmpty() ? "" : mg.getMonsters().get(0).getClass().getSimpleName();
                            if (canIntimidate && Math.random() < 0.2) {
                                mg.setState(RoamingMonsterGroup.AIState.FLEE);
                            } else if (mainMonster.equals("Mimic") || mainMonster.equals("Chest")) {
                                mg.setState(RoamingMonsterGroup.AIState.FLEE);
                            } else if (mainMonster.equals("Orc")) {
                                mg.setState(RoamingMonsterGroup.AIState.CHARGE);
                                mg.setStateTimer(1.5f);
                            } else {
                                mg.setState(RoamingMonsterGroup.AIState.CHASE);
                            }
                        }
                        break;
                    case CHASE:
                        if (!canSee && !canHear && dist > 7.0f) {
                            mg.setState(RoamingMonsterGroup.AIState.PATROL);
                        } else {
                            String mainMonster = mg.getMonsters().isEmpty() ? "" : mg.getMonsters().get(0).getClass().getSimpleName();
                            float chaseSpeed = 1.3f;
                            if (mainMonster.equals("Goblin")) chaseSpeed = 1.6f;
                            else if (mainMonster.equals("Specter") || mainMonster.equals("Vampire") || mainMonster.equals("Liche")) {
                                chaseSpeed = 0.5f;
                                mg.setStateTimer(mg.getStateTimer() - deltaTime);
                                if (mg.getStateTimer() <= 0) {
                                    int tpX = (int)playerX + (int)(Math.random() * 6 - 3);
                                    int tpZ = (int)playerZ + (int)(Math.random() * 6 - 3);
                                    if (tpX > 0 && tpX < maze.getWidth() && tpZ > 0 && tpZ < maze.getHeight() 
                                        && maze.getGrid()[tpX][tpZ].isWalkable()) {
                                        mg.setX(tpX + 0.5f);
                                        mg.setZ(tpZ + 0.5f);
                                        mg.setStateTimer(3.0f + (float)Math.random() * 2.0f);
                                    }
                                }
                            }
                            faceTowards(mg, dx, dz);
                            moveTowards(mg, dx, dz, dist, chaseSpeed * deltaTime, maze);
                        }
                        break;
                    case CHARGE:
                        mg.setStateTimer(mg.getStateTimer() - deltaTime);
                        moveForward(mg, 3.5f * deltaTime, maze);
                        if (mg.getStateTimer() <= 0) {
                            mg.setState(RoamingMonsterGroup.AIState.STUNNED);
                            mg.setStateTimer(2.0f);
                        }
                        break;
                    case STUNNED:
                        mg.setStateTimer(mg.getStateTimer() - deltaTime);
                        if (mg.getStateTimer() <= 0) {
                            mg.setState(RoamingMonsterGroup.AIState.ALERT);
                            mg.setAlertTimer(0.5f);
                        }
                        break;
                    case FLEE:
                        if (dist > 10.0f) mg.setState(RoamingMonsterGroup.AIState.PATROL);
                        else {
                            faceTowards(mg, -dx, -dz);
                            moveTowards(mg, -dx, -dz, dist, 2.8f * deltaTime, maze);
                        }
                        break;
                    case IDLE:
                    default:
                        break;
                }
            }

            // Check collision for encounter
            float checkDx = playerX - mg.getX();
            float checkDz = playerZ - mg.getZ();
            boolean collision = Math.sqrt(checkDx * checkDx + checkDz * checkDz) < 0.8f && hasLineOfSight(maze, playerX, playerZ, mg.getX(), mg.getZ());
            if (collision) {
                frameEncounters.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EncounterEvent(mg.getMonsters(), mg));
                it.remove();
            }
        }
        return frameEncounters;
    }

    private void faceTowards(RoamingMonsterGroup mg, float dx, float dz) {
        if (Math.abs(dx) > Math.abs(dz)) {
            if (dx > 0) mg.setFacingDirection(3); // East
            else mg.setFacingDirection(2); // West
        } else {
            if (dz > 0) mg.setFacingDirection(0); // South
            else mg.setFacingDirection(1); // North
        }
    }

    private void moveForward(RoamingMonsterGroup mg, float distance, Dungeon maze) {
        float nx = mg.getX();
        float nz = mg.getZ();
        
        switch (mg.getFacingDirection()) {
            case 0: nz += distance; break; // South
            case 1: nz -= distance; break; // North
            case 2: nx -= distance; break; // West
            case 3: nx += distance; break; // East
        }
        
        tryMove(mg, nx, nz, maze);
    }

    private void moveTowards(RoamingMonsterGroup mg, float dx, float dz, float dist, float speed, Dungeon maze) {
        if (dist > 0.001f) {
            float nx = mg.getX() + (dx / dist) * speed;
            float nz = mg.getZ() + (dz / dist) * speed;
            tryMove(mg, nx, nz, maze);
        }
    }

    private void tryMove(RoamingMonsterGroup mg, float nx, float nz, Dungeon maze) {
        float radius = 0.2f;
        boolean canMoveX = true;
        boolean canMoveZ = true;
        
        // Simple AABB for monsters
        if (nx > mg.getX()) {
            int hitX = (int)(nx + radius);
            if (!maze.getGrid()[Math.min(maze.getWidth()-1, hitX)][(int)mg.getZ()].isWalkable()) canMoveX = false;
        } else if (nx < mg.getX()) {
            int hitX = (int)(nx - radius);
            if (!maze.getGrid()[Math.max(0, hitX)][(int)mg.getZ()].isWalkable()) canMoveX = false;
        }
        
        if (nz > mg.getZ()) {
            int hitZ = (int)(nz + radius);
            if (!maze.getGrid()[(int)mg.getX()][Math.min(maze.getHeight()-1, hitZ)].isWalkable()) canMoveZ = false;
        } else if (nz < mg.getZ()) {
            int hitZ = (int)(nz - radius);
            if (!maze.getGrid()[(int)mg.getX()][Math.max(0, hitZ)].isWalkable()) canMoveZ = false;
        }
        
        if (canMoveX) mg.setX(nx);
        if (canMoveZ) mg.setZ(nz);
    }

    private boolean isPlayerInFOV(RoamingMonsterGroup mg, float dx, float dz) {
        // Dot product approach for FOV
        // facing vectors:
        float fx = 0, fz = 0;
        switch (mg.getFacingDirection()) {
            case 0: fz = 1; break;  // South
            case 1: fz = -1; break; // North
            case 2: fx = -1; break; // West
            case 3: fx = 1; break;  // East
        }
        
        float dist = (float)Math.sqrt(dx*dx + dz*dz);
        if (dist == 0) return true;
        
        float dot = (dx/dist)*fx + (dz/dist)*fz;
        return dot > 0.5f; // ~120 degree FOV (cos 60 = 0.5)
    }

    public static boolean hasLineOfSight(Dungeon maze, float x1, float z1, float x2, float z2) {
        // Bresenham's Line Algorithm
        int x = (int)x1;
        int y = (int)z1;
        int endX = (int)x2;
        int endY = (int)z2;

        int dx = Math.abs(endX - x);
        int dy = Math.abs(endY - y);
        int sx = x < endX ? 1 : -1;
        int sy = y < endY ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (x >= 0 && x < maze.getWidth() && y >= 0 && y < maze.getHeight()) {
                if (!maze.getGrid()[x][y].isWalkable() && !maze.getGrid()[x][y].hasDoor()) {
                    return false; // Vision blocked by wall (assuming doors don't block vision fully for simplicity, or we could check door state)
                }
                // Check if it's a closed door
                if (maze.getGrid()[x][y].hasDoor() && !maze.getGrid()[x][y].isDoorOpen()) {
                    return false;
                }
            } else {
                return false; // Out of bounds
            }

            if (x == endX && y == endY) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
        return true;
    }
}
