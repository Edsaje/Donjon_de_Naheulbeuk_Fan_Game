package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.Item.*;

public class Character {
    //attributs
    protected String name;
    protected String type; //classe du personnage
    protected int level;
    protected int healthPoint;
    protected int manaPoint;
    protected int attack;
    protected int magicAttack;
    protected int defense;
    protected int magicDefense;

    //constructeur
    public Character(String name, String type, int level, int healthPoint, int manaPoint,int attack, int magicAttack, int defense, int magicDefense){
        this.name = name;
        this.type = type;
        this.level = level;
        this.healthPoint = healthPoint;
        this.manaPoint = manaPoint;
        this.attack = attack;
        this.magicAttack = magicAttack;
        this.defense = defense;
        this.magicDefense = magicDefense;
    }

    //méthodes

    public void useSpecialSkill(Character target) {
        System.out.println(this.name + "n'a pas appris de compétence spéciale, le nul !");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMagicDefense() {
        return magicDefense;
    }

    public void setMagicDefense(int magicDefense) {
        this.magicDefense = magicDefense;
    }

    public int getMagicAttack() {
        return magicAttack;
    }

    public void setMagicAttack(int magicAttack) {
        this.magicAttack = magicAttack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getManaPoint() {
        return manaPoint;
    }

    public void setManaPoint(int manaPoint) {
        this.manaPoint = manaPoint;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", level=" + level +
                ", healthPoint=" + healthPoint +
                ", manaPoint=" + manaPoint +
                ", attack=" + attack +
                ", magicAttack=" + magicAttack +
                ", defense=" + defense +
                ", magicDefense=" + magicDefense +
                '}';
    }
}
