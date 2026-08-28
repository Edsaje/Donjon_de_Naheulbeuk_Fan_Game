package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.usable.potion.Potion;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.offensiveEquipment.OffensiveEquipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.defensiveEquipment.DefensiveEquipment;

public class ItemFactory {
    public static Item createItem(String itemId) {
        switch (itemId) {
            case "epee_base": return new OffensiveEquipment("Epee de Base", "Une epee simple.", EquipmentCategory.LIGHT_WEAPON, 1, 0);
            case "hache_deux_mains": return new OffensiveEquipment("Hache a 2 mains", "Lourde et tranchante.", EquipmentCategory.HEAVY_WEAPON, 2, 0);
            case "arc_bois": return new OffensiveEquipment("Arc en Bois", "Pour tirer de loin.", EquipmentCategory.RANGE_WEAPON, 1, 0);
            case "dague_emoussee": return new OffensiveEquipment("Dague Emoussee", "Petite lame courte.", EquipmentCategory.LIGHT_WEAPON, 1, 0);
            case "baton_magique": return new OffensiveEquipment("Baton Magique", "Canalise la magie.", EquipmentCategory.LIGHT_WEAPON, 0, 2);
            case "grosse_epee": return new OffensiveEquipment("Grosse Epee", "Tres grande epee.", EquipmentCategory.HEAVY_WEAPON, 2, 0);
            
            case "potion_mineure": return new Potion("Potion Mineure", "Restaure 20 PV.", 20);
            
            // Ressources de base pour le HUB
            case "bois": return new Material("Bois", "Morceau de bois pour la construction.");
            case "pierre": return new Material("Pierre", "Un bloc de pierre solide.");
            case "granite": return new Material("Granite", "Pierre de haute qualite.");
            case "foin": return new Material("Foin", "De la paille seche.");
            
            case "dent_gobelin": return new Material("Dent de Gobelin", "Une dent sale.");
            case "os_pourri": return new Material("Os Pourri", "Un vieil os.");
            case "epee_rouillee": return new OffensiveEquipment("Epee Rouillee", "Une epee usee.", EquipmentCategory.LIGHT_WEAPON, 2, 0);
            case "viande_orque": return new Material("Viande d'Orque", "Comestible.");
            case "cuir_orque": return new Material("Cuir d'Orque", "Utile.");
            case "fil_araignee": return new Material("Fil d'Araignee", "Collant.");
            case "crochet_venimeux": return new Material("Crochet Venimeux", "Piquant.");
            case "cuir_troll": return new Material("Cuir de Troll", "Robuste.");
            case "massue_lourde": return new OffensiveEquipment("Massue Lourde", "Une grosse massue.", EquipmentCategory.HEAVY_WEAPON, 10, 0);
            case "parchemin_mystique": return new Material("Parchemin Mystique", "Runes.");
            case "morceau_armure": return new Material("Morceau d'Armure", "A reforger.");
            case "epee_maudite": return new OffensiveEquipment("Epee Maudite", "Malefique.", EquipmentCategory.HEAVY_WEAPON, 25, 0);
            case "queue_rat": return new Material("Queue de Rat", "Degoutant.");
            case "viande_douteuse": return new Material("Viande Douteuse", "Beurk.");
            case "tissu_dechire": return new Material("Tissu Dechire", "Vieux.");
            case "papier_toilette_legendaire": return new Material("Papier Toilette", "Le PQ !");
            case "eponge_magique": return new Material("Eponge", "Gratte bien.");
            case "raviolis_divins": return new Material("Raviolis", "Cabossee.");
            case "potion": return new Potion("Potion", "Restaure 50 PV.", 50);
            default: return new Material(itemId, "Objet inconnu.");
        }
    }
}