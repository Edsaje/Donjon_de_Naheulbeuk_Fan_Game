package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import java.io.Serializable;
import java.util.List;

public class RoamingMonsterGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum AIState { PATROL, CHASE, IDLE }

    private float x;
    private float z;
    private List<Character> monsters;
    private boolean isBoss;
    private AIState state = AIState.PATROL;

    public RoamingMonsterGroup(float x, float z, List<Character> monsters, boolean isBoss) {
        this.x = x;
        this.z = z;
        this.monsters = monsters;
        this.isBoss = isBoss;
        if (isBoss) {
            this.state = AIState.IDLE;
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
}
