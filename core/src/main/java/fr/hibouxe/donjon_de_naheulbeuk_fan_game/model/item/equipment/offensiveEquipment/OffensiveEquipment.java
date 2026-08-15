package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.offensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

public class OffensiveEquipment extends Equipment{


    public OffensiveEquipment(String name, String description, EquipmentCategory category, int attackBonus, int magicAttackBonus){
        super(name, description, EquipmentSlot.WEAPON, category, attackBonus, magicAttackBonus, 0,0,0);
    }

    public OffensiveEquipment(String name, String description, EquipmentCategory category, String requiredJob, int attackBonus, int magicAttackBonus){
        super(name, description, EquipmentSlot.WEAPON, category, requiredJob, attackBonus, magicAttackBonus,0,0,0);
    }

    @Override
    public boolean use(Character target){
        return target.equip(this);
    }
}
