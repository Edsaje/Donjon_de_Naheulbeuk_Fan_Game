package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import java.util.List;

public interface ICombatView {
    void displayBattleStatus(List<Character> monsters, Team team);
    int askBattleAction(Character attacker);
    Skill askSkill(Character attacker, List<Character> monsters);
    Character askMonsterTarget(List<Character> monsters);
    void displayTurn(String characterName);
    void displayVictory();
    void displayDefeat();
    void displayMessage(String message);
    void displayInventory(Team team);
    int askItemIndex();
    Character askItemTarget(Team team);
}
