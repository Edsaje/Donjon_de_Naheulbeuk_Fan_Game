package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import java.io.Serializable;
import java.util.List;

public class RoamingMonsterGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum AIState { PATROL, CHASE, IDLE, SLEEPING, ALERT, FLEE, CHARGE, STUNNED }

    private float x;
    private float z;
    private List<Character> monsters;
    private boolean isBoss;
    private AIState state = AIState.PATROL;
    
    // AI Parameters
    private int facingDirection = 0; // 0=Sud, 1=Nord, 2=Ouest, 3=Est
    private float alertTimer = 0f;
    private float stateTimer = 0f; // For patrol wandering
    private float targetX = -1f;
    private float targetZ = -1f;

    public RoamingMonsterGroup(float x, float z, List<Character> monsters, boolean isBoss) {
        this.x = x;
        this.z = z;
        this.monsters = monsters;
        this.isBoss = isBoss;
        
        if (isBoss) {
            this.state = AIState.IDLE;
        } else {
            // Randomly some monsters might be sleeping
            if (Math.random() < 0.3) {
                this.state = AIState.SLEEPING;
            }
            this.facingDirection = (int)(Math.random() * 4);
        }
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getZ() { return z; }
    public void setZ(float z) { this.z = z; }

    public List<Character> getMonsters() { return monsters; }
    public void setMonsters(List<Character> monsters) { this.monsters = monsters; }

    public boolean isBoss() { return isBoss; }
    public void setBoss(boolean boss) { this.isBoss = boss; }

    public AIState getState() { return state; }
    public void setState(AIState state) { this.state = state; }

    public int getFacingDirection() { return facingDirection; }
    public void setFacingDirection(int facingDirection) { this.facingDirection = facingDirection; }

    public float getAlertTimer() { return alertTimer; }
    public void setAlertTimer(float alertTimer) { this.alertTimer = alertTimer; }

    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float stateTimer) { this.stateTimer = stateTimer; }

    public float getTargetX() { return targetX; }
    public void setTargetX(float targetX) { this.targetX = targetX; }

    public float getTargetZ() { return targetZ; }
    public void setTargetZ(float targetZ) { this.targetZ = targetZ; }
    
    public boolean isDefeated() {
        if (monsters == null || monsters.isEmpty()) return true;
        for (Character m : monsters) {
            if (m.getHealthPoint() > 0) return false;
        }
        return true;
    }
}
