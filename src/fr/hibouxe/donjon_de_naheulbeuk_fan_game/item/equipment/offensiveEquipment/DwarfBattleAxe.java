package fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.offensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentCategory;

/**
 * La Hache de Choc du Nain : Arme lourde dévastatrice réservée au Nain.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class DwarfBattleAxe extends OffensiveEquipment {

    /**
     * Initialise la Hache de Choc avec ses bonus d'attaque et sa restriction de classe (Dwarf).
     */
    public DwarfBattleAxe() {
        super("Hache de Choc du Nain", "Une lourde hache à double tranchant gravée de runes. Réservée au Nain (+8 Attaque).",
              EquipmentCategory.HEAVY_WEAPON, "Dwarf", 8, 0);
    }
}
