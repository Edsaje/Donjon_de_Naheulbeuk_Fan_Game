package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random;

import java.io.Serializable;

public interface IRandomProvider extends Serializable {
    int nextInt(int bound);
    int nextInt(int origin, int bound);
    double nextDouble();
}
