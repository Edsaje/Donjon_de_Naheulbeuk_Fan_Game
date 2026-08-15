package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
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
            dialogs.add("Ranger : Hé l'Elfe, lève-toi, on doit sortir d'ici.");
            dialogs.add("Elfe : *gémissement* J'ai trop mal à la tête... je peux à peine marcher.");
            dialogs.add("Ranger : Bon, rejoins le groupe, mais il va falloir te rafistoler avant qu'on bouge d'ici.");
            
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Elf elfe = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Elf();
            elfe.setHealthPoint(1);
            team.getMembers().add(elfe);
            elfJoined = true;
            dialogs.add("\n[L'Elfe a rejoint le groupe, mais elle est gravement blessée !]");
        } else {
            dialogs.add("\nL'Elfe est trop blessée pour avancer. Appuyez sur ECHAP pour ouvrir le menu, allez dans SAC, et utilisez la Potion de Soin sur l'Elfe.");
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
