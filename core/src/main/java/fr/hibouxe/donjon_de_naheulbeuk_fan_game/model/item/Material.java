package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

public class Material extends Item {
    public Material(String name, String description) {
        super(name, description);
    }

    @Override
    public boolean use(Character target) {
        return false;
    }
}
