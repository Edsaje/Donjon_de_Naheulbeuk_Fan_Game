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
        f4.getIntroDialogues().add("Ranger : On est presque devant le bureau de Zangdar ! Préparez vos armes !");
        blueprints.put(4, f4);

        // Floor 5 config
        FloorBlueprint f5 = new FloorBlueprint(31, 0, 0, 0);
        f5.setBossType("Golem");
        f5.getIntroDialogues().add("=== ÉTAGE 5 : L'ANTICHAMBRE DU BUREAU DE ZANGDAR ===");
        f5.getIntroDialogues().add("Magicienne : Attention ! C'est un Golem de Fer ! C'est une machine à baffes insensible aux armes simples !");
        f5.getIntroDialogues().add("Nain : YAAAAAAAAAH ! (Il charge la hache en avant, frappe l'acier et se tord les poignets !)");
        f5.getIntroDialogues().add("Zangdar (depuis son balcon) : Insolents ! Misérables cloportes ! Vous n'emporterez jamais la statuette de Gladeulfeurh ! Golem de fer, réduis-les en bouillie !");
        blueprints.put(5, f5);
    }

    private FloorBlueprint getBlueprintForFloor(int floorNumber) {
        if (blueprints.containsKey(floorNumber)) {
            return blueprints.get(floorNumber);
        }
        // Default generated blueprint
        FloorBlueprint bp = new FloorBlueprint(21 + (floorNumber * 2), 5 + floorNumber, 3, 1);
        bp.getIntroDialogues().add("=== DESCENTE À L'ÉTAGE " + floorNumber + " ===");
        bp.getIntroDialogues().add("Narrateur : La compagnie avance prudemment dans les ténèbres...");
        return bp;
    }

    @Override
    public boolean prepareFloor(int floorNumber, Team team) {
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

        // 2. Générer la structure
        this.generateHybridDungeon();

        // 3. Placer l'équipe
        int[] startPos = getFirstWalkablePosition();
        team.setX(startPos[0]);
        team.setY(startPos[1]);

        // 4. Peuplement depuis le blueprint
        if ("Golem".equals(bp.getBossType())) {
            int[] bossPos = getFirstWalkablePosition(); 
            List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> bossList = new ArrayList<>();
            bossList.add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem());
            this.getRoamingMonsters().add(new RoamingMonsterGroup(bossPos[0], bossPos[1], bossList, true));
        } else {
            generateMonsters(bp.getNumMonsters(), team.getX(), team.getY());
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
