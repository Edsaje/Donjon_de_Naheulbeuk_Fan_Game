package fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.defensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentCategory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot;

/**
 * Le Pagne Sauvage du Barbare : Pagne en peau de loup réservé au Barbare.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class BarbarianLoincloth extends DefensiveEquipment {

    /**
     * Initialise le Pagne Sauvage avec ses bonus de défense et sa restriction de classe (Barbarian).
     */
    public BarbarianLoincloth() {
        super("Pagne Sauvage en Cuir", "Un pagne en peau de loup tannée à la bière. Réservé au Barbare (+5 Défense, +2 Défense Magique).",
              EquipmentSlot.CHEST, EquipmentCategory.WRAP_SKIRT, "Barbarian", 5, 2);
    }
}
