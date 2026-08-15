package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity;

public class SkillResult {
    private boolean success;
    private int amount; // Damage or Heal amount
    private String type; // "HEAL", "DAMAGE", "ERROR"

    public SkillResult(boolean success, int amount, String type) {
        this.success = success;
        this.amount = amount;
        this.type = type;
    }

    public boolean isSuccess() { return success; }
    public int getAmount() { return amount; }
    public String getType() { return type; }
}
