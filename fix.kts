import java.io.File
import java.nio.file.Files
import java.nio.charset.StandardCharsets

val files = listOf(
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/controller/ExplorationController.java",
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/dungeon/Cell.java",
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/dungeon/DungeonGenerator.java",
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/dungeon/TutorialDungeon.java",
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Barbarian.java",
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Dwarf.java",
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Elf.java",
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Ogre.java",
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Ranger.java",
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/model/entity/playerClasses/Thief.java",
    "core/src/main/java/fr/hibouxe/donjon_de_naheulbeuk_fan_game/view/graphic/renderers/DungeonSceneRenderer.java"
)

val replacements = mapOf(
    "ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©" to "é",
    "ÃƒÆ’Ã†â€™Ãƒâ€ ââ‚¬â„¢ÃƒÆ’ââ‚¬Â âââ€šÂ¬ââ€žÂ¢ÃƒÆ’Ã†â€™âââ€šÂ¬Ã…Â¡ÃƒÆ’ââ‚¬Å¡Ãƒâ€šÃ‚Â©" to "é",
    "ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©" to "é",
    "ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢" to "â",
    "ÃƒÆ’Ã†â€™ÃƒÆ’Ã‚Â " to "à",
    "ÃƒÆ’Ã‚Â©" to "é",
    "ÃƒÂ©" to "é",
    "ÃƒÆ’Ã‚Â¨" to "è",
    "ÃƒÂ¨" to "è",
    "ÃƒÆ’Ã‚Â " to "à",
    "ÃƒÂ " to "à",
    "ÃƒÂ¢" to "â",
    "ÃƒÂ®" to "î",
    "ÃƒÂª" to "ê",
    "ÃƒÂ´" to "ô",
    "ÃƒÂ»" to "û",
    "ÃƒÂ§" to "ç",
    "Ã©" to "é",
    "Ã¨" to "è",
    "Ã " to "à"
)

for (f in files) {
    val file = File(f)
    if (!file.exists()) continue
    var content = String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8)
    var changed = false
    
    for ((old, newStr) in replacements) {
        if (content.contains(old)) {
            content = content.replace(old, newStr)
            changed = true
        }
    }
    
    if (changed) {
        Files.write(file.toPath(), content.toByteArray(StandardCharsets.UTF_8))
        println("Fixed: \$f")
    }
}
