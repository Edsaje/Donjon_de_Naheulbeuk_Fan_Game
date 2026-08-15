package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import java.io.Serializable;

public interface ICellEvent extends Serializable {
    /**
     * Executes the cell event.
     * @return EventResult containing if the event blocks normal movement and any dialogs to display.
     */
    EventResult trigger(Team team);

    default void onItemUsed(Item item, Character target, Dungeon maze) {}
}
