package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.components;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

public class EquipmentComponent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<EquipmentSlot, Equipment> equipmentSlots = new EnumMap<>(EquipmentSlot.class);

    public Map<EquipmentSlot, Equipment> getEquipmentSlots() {
        return equipmentSlots;
    }

    public boolean equip(Equipment equipment, Character character) {
        if (equipment == null) return false;
        if (!equipment.canBeEquippedBy(character)) {
            return false;
        }
        equipmentSlots.put(equipment.getSlot(), equipment);
        return true;
    }

    public boolean unequip(EquipmentSlot slot, Team team) {
        Equipment tmp = equipmentSlots.get(slot);
        if (tmp == null) {
            return false;
        } else {
            if (team.addItem(tmp)) {
                equipmentSlots.remove(slot);
                return true;
            } else {
                return false;
            }
        }
    }
}
