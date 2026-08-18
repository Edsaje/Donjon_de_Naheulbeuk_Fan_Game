package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Ranger;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class SaveDataTest {

    @Test
    void testSerialization() throws Exception {
        Team team = new Team();
        team.getMembers().add(new Ranger());
        TutorialDungeon dungeon = new TutorialDungeon();
        
        SaveData data = new SaveData(team, dungeon, 2, true);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(data);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        SaveData loadedData = (SaveData) ois.readObject();
        ois.close();
        
        assertNotNull(loadedData);
        assertEquals(2, loadedData.getCurrentFloor());
        assertNotNull(loadedData.getDungeon());
    }
}