package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class NaheulbeukDungeon extends Dungeon {

    private final Map<Integer, FloorBlueprint> blueprints = new HashMap<>();

    public NaheulbeukDungeon() {
        super(21, 21); // Taille de base
        initBlueprints();
    }

    private void initBlueprints() {
        // Floor 4 config
        FloorBlueprint f4 = new FloorBlueprint(29, 8, 3, 1);
        f4.getIntroDialogues().add("Ranger : On est presque devant le bureau de Zangdar ! Prparez vos armes !");
        blueprints.put(4, f4);

        // Floor 5 config
        FloorBlueprint f5 = new FloorBlueprint(31, 0, 0, 0);
        f5.setBossType("Golem");
        f5.getIntroDialogues().add("=== TAGE 5 : L'ANTICHAMBRE DU BUREAU DE ZANGDAR ===");
        f5.getIntroDialogues().add("Magicienne : Attention ! C'est un Golem de Fer ! C'est une machine  baffes insensible aux armes simples !");
        f5.getIntroDialogues().add("Nain : YAAAAAAAAAH ! (Il charge la hache en avant, frappe l'acier et se tord les poignets !)");
        f5.getIntroDialogues().add("Zangdar (depuis son balcon) : Insolents ! Misrables cloportes ! Vous n'emporterez jamais la statuette de Gladeulfeurh ! Golem de fer, rduis-les en bouillie !");
        blueprints.put(5, f5);
    }

    private FloorBlueprint getBlueprintForFloor(int floorNumber) {
        if (blueprints.containsKey(floorNumber)) {
            return blueprints.get(floorNumber);
        }
        // Default generated blueprint
        FloorBlueprint bp = new FloorBlueprint(21 + (floorNumber * 2), 5 + floorNumber, 3, 1);
        bp.getIntroDialogues().add("=== DESCENTE  L'TAGE " + floorNumber + " ===");
        bp.getIntroDialogues().add("Narrateur : La compagnie avance prudemment dans les tnbres...");
        return bp;
    }

    @Override
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character getRandomMonster(int floorNumber, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.IRandomProvider random, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository repository) {
        String monsterId;
        double roll = random.nextDouble();
        
        // Spawn tables based on floor
        if (floorNumber <= 2) {
            // tages 1-2 : Gobelins trs communs, un peu de rats et araignes
            if (roll < 0.60) monsterId = "goblin";
            else if (roll < 0.80) monsterId = "mutant_rat";
            else if (roll < 0.95) monsterId = "giant_spider";
            else monsterId = "orc"; // 5% chance Orc
        } else if (floorNumber <= 4) {
            // tages 3-4 : Orques, Morts-vivants, Squelettes
            if (roll < 0.35) monsterId = "orc";
            else if (roll < 0.60) monsterId = "skeleton";
            else if (roll < 0.85) monsterId = "zombie";
            else if (roll < 0.95) monsterId = "giant_spider";
            else monsterId = "easter_egg_tp"; // 5% chance Rouleau PQ
        } else {
            // tages 5+ (Post-game ou fin) : Trolls, Sorciers, Guerriers maudits
            if (roll < 0.30) monsterId = "troll";
            else if (roll < 0.60) monsterId = "cursed_warrior";
            else if (roll < 0.90) monsterId = "sorcerer";
            else if (roll < 0.98) monsterId = "easter_egg_sponge";
            else monsterId = "easter_egg_ravioli"; // 2% Ravioli
        }

        return new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Monster(repository.getMonsterData(monsterId));
    }

    @Override
    public boolean prepareFloor(int floorNumber, Team team, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository repository) {
        FloorBlueprint bp = getBlueprintForFloor(floorNumber);

        // 1. Adapter la taille
        this.setWidth(bp.getSize());
        this.setHeight(bp.getSize());
        this.setGrid(new Cell[bp.getSize()][bp.getSize()]);
        for (int x = 0; x < bp.getSize(); x++) {
            for (int y = 0; y < bp.getSize(); y++) {
                this.getGrid()[x][y] = new Cell(x, y);
            }
        }

        // 2. Gnrer la structure
        this.generateHybridDungeon();

        // 3. Placer l'quipe
        int[] startPos = getSpawnPosition();
        team.setX(startPos[0]);
        team.setY(startPos[1]);

        // 4. Peuplement depuis le blueprint
        if ("Golem".equals(bp.getBossType())) {
            int[] bossPos = getBossSpawnPosition(); 
            List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> bossList = new ArrayList<>();
            bossList.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem());
            this.getRoamingMonsters().add(new RoamingMonsterGroup(bossPos[0], bossPos[1], bossList, true));
        } else {
            generateMonsters(bp.getNumMonsters(), team.getX(), team.getY(), repository, floorNumber);
        }
        generateItems(bp.getNumItems());
        generateStairs(bp.getNumStairs());
        
        return false;
    }

    @Override
    public List<String> getFloorIntroDialogues(int floorNumber) {
        return getBlueprintForFloor(floorNumber).getIntroDialogues();
    }

    @Override
    public boolean isExpeditionComplete(int floorNumber) {
        FloorBlueprint bp = getBlueprintForFloor(floorNumber);
        if ("Golem".equals(bp.getBossType())) {
            for (RoamingMonsterGroup group : getRoamingMonsters()) {
                if (group.isBoss()) {
                    for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character c : group.getMonsters()) {
                        if (c instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem) {
                            return false; 
                        }
                    }
                }
            }
            return true; // Golem is dead
        }
        return false;
    }
}
