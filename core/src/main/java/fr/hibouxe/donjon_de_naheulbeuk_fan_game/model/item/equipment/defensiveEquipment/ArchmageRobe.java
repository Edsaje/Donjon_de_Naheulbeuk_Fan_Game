package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.defensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;

/**
 * La Robe d'Archimage : Tissu enchant rserv  la Magicienne.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class ArchmageRobe extends DefensiveEquipment {

    /**
     * Initialise la Robe d'Archimage avec ses bonus magiques et sa restriction (Magician).
     */
    public ArchmageRobe() {
        super("Robe de l'Archimage Tholsadum", "C'est une relique, un vtement pour les sorciers ! Avec fonction chauffante (+2 Dfense, +8 Dfense Magique).",
              EquipmentSlot.CHEST, EquipmentCategory.CLOTH_ROBE, "Magicienne", 2, 8);
    }
}
