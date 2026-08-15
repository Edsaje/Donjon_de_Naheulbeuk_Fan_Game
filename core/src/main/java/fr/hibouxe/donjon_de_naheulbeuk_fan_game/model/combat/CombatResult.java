package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat;

public class CombatResult {
    private final int damage;
    private final boolean targetDead;

    public CombatResult(int damage, boolean targetDead) {
        this.damage = damage;
        this.targetDead = targetDead;
    }

    public int getDamage() { return damage; }
    public boolean isTargetDead() { return targetDead; }
}
