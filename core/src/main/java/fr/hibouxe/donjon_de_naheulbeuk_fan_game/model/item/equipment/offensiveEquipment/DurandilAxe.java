package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.offensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory;

/**
 * La Hache de Choc du Nain : Arme lourde dvastatrice rserve au Nain.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class DurandilAxe extends OffensiveEquipment {

    /**
     * Initialise la Hache de Choc avec ses bonus d'attaque et sa restriction de classe (Dwarf).
     */
    public DurandilAxe() {
        super("Hache de jet Durandil", "J'ai trouv une super hache Durandil, en farfouillant dans un p'tit magasin ! Rserve au Nain (+8 Attaque).",
              EquipmentCategory.HEAVY_WEAPON, "Nain", 8, 0);
    }
}
