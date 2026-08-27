package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.RoamingMonsterGroup;
import java.util.List;

public class EncounterEvent {
    private final List<Character> monsters;
    private final RoamingMonsterGroup sourceGroup;

    public EncounterEvent(List<Character> monsters, RoamingMonsterGroup sourceGroup) {
        this.monsters = monsters;
        this.sourceGroup = sourceGroup;
    }

    public List<Character> getMonsters() {
        return monsters;
    }
    
    public RoamingMonsterGroup getSourceGroup() {
        return sourceGroup;
    }
}
