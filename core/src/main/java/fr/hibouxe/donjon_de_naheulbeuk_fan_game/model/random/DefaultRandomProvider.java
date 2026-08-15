package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random;

import java.util.Random;

public class DefaultRandomProvider implements IRandomProvider {
    private static final long serialVersionUID = 1L;
    private final Random random = new Random();

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public int nextInt(int origin, int bound) {
        return origin + random.nextInt(bound - origin);
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }
}
