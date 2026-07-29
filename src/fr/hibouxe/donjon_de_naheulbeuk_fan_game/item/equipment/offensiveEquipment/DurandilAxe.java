package fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.offensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentCategory;

/**
 * La Hache de Choc du Nain : Arme lourde dévastatrice réservée au Nain.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class DurandilAxe extends OffensiveEquipment {

    /**
     * Initialise la Hache de Choc avec ses bonus d'attaque et sa restriction de classe (Dwarf).
     */
    public DurandilAxe() {
        super("Hache de jet Durandil", "J'ai trouvé une super hache Durandil, en farfouillant dans un p'tit magasin ! Réservée au Nain (+8 Attaque).",
              EquipmentCategory.HEAVY_WEAPON, "Nain", 8, 0);
    }
}
