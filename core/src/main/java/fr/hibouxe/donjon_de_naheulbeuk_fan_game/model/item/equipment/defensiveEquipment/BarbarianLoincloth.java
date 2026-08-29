package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.defensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;

/**
 * Le Pagne Sauvage du Barbare : Pagne en peau de loup rserv au Barbare.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class BarbarianLoincloth extends DefensiveEquipment {

    /**
     * Initialise le Pagne Sauvage avec ses bonus de dfense et sa restriction de classe (Barbarian).
     */
    public BarbarianLoincloth() {
        super("Pagne Sauvage en Cuir", "Un pagne en peau de loup tanne  la bire. Rserv au Barbare (+5 Dfense, +2 Dfense Magique).",
              EquipmentSlot.CHEST, EquipmentCategory.WRAP_SKIRT, "Barbare", 5, 2);
    }
}
