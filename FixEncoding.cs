using System;
using System.IO;
using System.Text;
using System.Collections.Generic;

public class FixEncoding
{
    public static void Main()
    {
        string[] files = new string[]
        {
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
        };

        var replacements = new Dictionary<string, string>()
        {
            {"ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©", "é"},
            {"ÃƒÆ’Ã†â€™Ãƒâ€ ââ‚¬â„¢ÃƒÆ’ââ‚¬Â âââ€šÂ¬ââ€žÂ¢ÃƒÆ’Ã†â€™âââ€šÂ¬Ã…Â¡ÃƒÆ’ââ‚¬Å¡Ãƒâ€šÃ‚Â©", "é"},
            {"ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©", "é"},
            {"ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢", "â"},
            {"ÃƒÆ’Ã†â€™ÃƒÆ’Ã‚Â ", "à"},
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
            {"Ã ", "à"},
            {"ǟ'Ń?Tǟ?s'", "é"},
            {"ǟ'Ń?Tǟ?ǽ'", "à"},
            {"ǟǽ?sǽ?zǟ'Ń?Tǟ?s'", "é"}
        };

        foreach (var file in files)
        {
            if (!File.Exists(file)) continue;

            string content = File.ReadAllText(file, new UTF8Encoding(false));
            bool changed = false;

            foreach (var kvp in replacements)
            {
                if (content.Contains(kvp.Key))
                {
                    content = content.Replace(kvp.Key, kvp.Value);
                    changed = true;
                }
            }

            if (changed)
            {
                File.WriteAllText(file, content, new UTF8Encoding(false));
                Console.WriteLine("Fixed: " + file);
            }
        }
    }
}
