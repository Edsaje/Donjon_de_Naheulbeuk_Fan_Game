package fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

public interface Equipabble {
    EquipmentSlot getSlot();

    EquipmentCategory getCategory();

    boolean canBeEquippedBy(Character hero);
}
