package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import java.util.List;

public interface ICombatView {
    void displayBattleStatus(List<Character> monsters, Team team);
    int askBattleAction(Character attacker);
    void showActionMenu(Character combatant, List<String> actions);
    Skill askSkill(Character attacker, List<Character> monsters);
    Character askMonsterTarget(List<Character> monsters);
    void displayTurn(String characterName);
    void displayVictory(int xp, int gold, List<String> loots);
    void displayDefeat();
    void displayMessage(String message);
    void displayInventory(Team team);
    int askItemIndex();
    Character askItemTarget(Team team);
    void displayDialogue(String message);
    void clearMessages();
    int getMenuSelection();
    void setMenuRequest(String title, String[] options);
    void playHitAnimation(Character target, int damage, String vfxType, String sfxPath);
}
