package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.components;

import java.io.Serializable;

public class StatComponent implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maxHealthPoint;
    private int healthPoint;
    private int attack;
    private int magicAttack;
    private int defense;
    private int magicDefense;
    private int speed;
    private String resourceName;
    private int currentResource;
    private int maxResource;

    public StatComponent(int healthPoint, int maxHealthPoint, int attack, int magicAttack, int defense, int magicDefense, int speed, int currentResource, int maxResource, String resourceName) {
        this.healthPoint = healthPoint;
        this.maxHealthPoint = maxHealthPoint;
        this.attack = attack;
        this.magicAttack = magicAttack;
        this.defense = defense;
        this.magicDefense = magicDefense;
        this.speed = speed;
        this.currentResource = currentResource;
        this.maxResource = maxResource;
        this.resourceName = resourceName;
    }

    public int getMaxHealthPoint() { return maxHealthPoint; }
    public void setMaxHealthPoint(int maxHealthPoint) { this.maxHealthPoint = maxHealthPoint; }

    public int getHealthPoint() { return healthPoint; }
    public void setHealthPoint(int healthPoint) { this.healthPoint = healthPoint; }

    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }

    public int getMagicAttack() { return magicAttack; }
    public void setMagicAttack(int magicAttack) { this.magicAttack = magicAttack; }

    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }

    public int getMagicDefense() { return magicDefense; }
    public void setMagicDefense(int magicDefense) { this.magicDefense = magicDefense; }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }

    public int getCurrentResource() { return currentResource; }
    public void setCurrentResource(int currentResource) { this.currentResource = currentResource; }

    public int getMaxResource() { return maxResource; }
    public void setMaxResource(int maxResource) { this.maxResource = maxResource; }
}
