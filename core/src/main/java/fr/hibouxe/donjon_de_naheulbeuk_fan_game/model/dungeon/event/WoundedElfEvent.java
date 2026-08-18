package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;

import java.util.ArrayList;
import java.util.List;

public class WoundedElfEvent implements ICellEvent {
    private static final long serialVersionUID = 1L;
    private boolean elfJoined = false;

    @Override
    public EventResult trigger(Team team) {
        List<String> dialogs = new ArrayList<>();
        if (!elfJoined) {
            dialogs.add("TUTO_FLOOR_2_ELF_DEAD_1");
            dialogs.add("TUTO_FLOOR_2_ELF_DEAD_2");
            dialogs.add("TUTO_FLOOR_2_ELF_DEAD_3");
            
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Elf elfe = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Elf();
            elfe.setHealthPoint(1);
            team.getMembers().add(elfe);
            elfJoined = true;
        } else {
            dialogs.add("TUTO_FLOOR_2_ELF_WOUNDED");
        }
        return new EventResult(true, dialogs);
    }

    @Override
    public void onItemUsed(Item item, Character target, Dungeon maze) {
        if (target.getClass().getSimpleName().equals("Elf") && item instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.usable.potion.Potion) {
            target.setHealthPoint(target.getMaxHealthPoint()); // Heal the elf
            for (int x = 0; x < maze.getWidth(); x++) {
                for (int y = 0; y < maze.getHeight(); y++) {
                    if (maze.getGrid()[x][y].getEvent() == this) {
                        maze.getGrid()[x][y].setEvent(null);
                    }
                }
            }
        }
    }
    
    public boolean hasElfJoined() {
        return elfJoined;
    }
}
