package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat;

public class CombatResult {
    private final String message;
    private final String damageMessage;

    public CombatResult(String message, String damageMessage) {
        this.message = message;
        this.damageMessage = damageMessage;
    }

    public String getMessage() {
        return message;
    }

    public String getDamageMessage() {
        return damageMessage;
    }
}
