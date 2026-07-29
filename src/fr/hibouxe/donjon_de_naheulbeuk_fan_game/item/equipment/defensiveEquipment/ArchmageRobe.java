package fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.defensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentCategory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot;

/**
 * La Robe d'Archimage : Tissu enchanté réservé à la Magicienne.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class ArchmageRobe extends DefensiveEquipment {

    /**
     * Initialise la Robe d'Archimage avec ses bonus magiques et sa restriction (Magician).
     */
    public ArchmageRobe() {
        super("Robe d'Archimage", "Robe en soie tressée de fils d'argent infusés d'éthanol magique. Réservée à la Magicienne (+2 Défense, +8 Défense Magique).",
              EquipmentSlot.CHEST, EquipmentCategory.CLOTH_ROBE, "Magician", 2, 8);
    }
}
