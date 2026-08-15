package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.components;

import java.io.Serializable;

public class ProgressionComponent implements Serializable {
    private static final long serialVersionUID = 1L;

    private int level;
    private int xp = 0;
    private int xpToNextLevel = 100;

    public ProgressionComponent(int level) {
        this.level = level;
    }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }

    public int getXpToNextLevel() { return xpToNextLevel; }
    public void setXpToNextLevel(int xpToNextLevel) { this.xpToNextLevel = xpToNextLevel; }

    public int gainXp(int amount) {
        this.xp += amount;
        int levelsGained = 0;
        while (this.xp >= this.xpToNextLevel) {
            levelsGained += levelUp();
        }
        return levelsGained;
    }

    public int levelUp() {
        this.level++;
        this.xp -= this.xpToNextLevel;
        this.xpToNextLevel = (int) (100 * Math.pow(this.level, 1.5));
        return 1;
    }
}
