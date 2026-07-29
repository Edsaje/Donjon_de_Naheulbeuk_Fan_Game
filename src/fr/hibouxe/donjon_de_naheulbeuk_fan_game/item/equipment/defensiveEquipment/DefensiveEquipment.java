package fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.defensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.Equipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentCategory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

public class DefensiveEquipment extends Equipment {

    public DefensiveEquipment(String name, String description, EquipmentSlot slot, EquipmentCategory category, int defenseBonus, int magicDefenseBonus){
        super(name, description, slot, category, 0, 0,defenseBonus, magicDefenseBonus, 0);
    }

    public DefensiveEquipment(String name, String description, EquipmentSlot slot, EquipmentCategory category, String requiredJob, int defenseBonus, int magicDefenseBonus){
        super(name, description, slot, category, requiredJob, 0, 0, defenseBonus, magicDefenseBonus, 0);
    }

    @Override
    public void use(Character target, Menu menu){
        target.equip(this, menu);
    }
}