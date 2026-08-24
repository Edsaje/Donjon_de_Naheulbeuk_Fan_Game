package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.MonsterDef;
import java.util.List;

public interface IMonsterRepository {
    MonsterDef getMonsterData(String id);
}
