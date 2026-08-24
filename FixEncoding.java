import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class FixEncoding {
    public static void main(String[] args) throws Exception {
        String[] files = {
            "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/controller/ExplorationController.java",
            "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/dungeon/Cell.java",
            "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/dungeon/DungeonGenerator.java",
            "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/dungeon/TutorialDungeon.java",
            "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Barbarian.java",
            "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Dwarf.java",
            "core\src\main\java\fr\hibouxe\donjon_de_naheulbeuk_fan_game\model\entity\playerClasses\Elf.java",
            "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Ogre.java",
            "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Ranger.java",
            "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Thief.java",
            "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/view/graphic/renderers/DungeonSceneRenderer.java"
        };
        
        for (String f : files) {
            f = f.replace("\\", "/");
            File file = new File(f);
            if (!file.exists()) continue;
            
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            boolean changed = false;
            
            String[][] replacements = {
                {"ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©", "é"},
                {"ÃƒÆ’Ã†â€™Ãƒâ€ ââ‚¬â„¢ÃƒÆ’ââ‚¬Â âââ€šÂ¬ââ€žÂ¢ÃƒÆ’Ã†â€™âââ€šÂ¬Ã…Â¡ÃƒÆ’ââ‚¬Å¡Ãƒâ€šÃ‚Â©", "é"},
                {"ÃƒÆ’Ã‚Â©", "é"},
                {"ÃƒÂ©", "é"},
                {"ÃƒÆ’Ã‚Â¨", "è"},
                {"ÃƒÂ¨", "è"},
                {"ÃƒÆ’Ã‚Â ", "à"},
                {"ÃƒÂ ", "à"},
                {"ÃƒÂ¢", "â"},
                {"ÃƒÂ®", "î"},
                {"ÃƒÂª", "ê"},
                {"ÃƒÂ´", "ô"},
                {"ÃƒÂ»", "û"},
                {"ÃƒÂ§", "ç"},
                {"Ã©", "é"},
                {"Ã¨", "è"},
                {"Ã ", "à"}
            };
            
            for (String[] r : replacements) {
                if (content.contains(r[0])) {
                    content = content.replace(r[0], r[1]);
                    changed = true;
                }
            }
            
            if (changed) {
                Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
                System.out.println("Fixed: " + f);
            }
        }
    }
}
