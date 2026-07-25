package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity;

import java.util.List;
import java.util.ArrayList;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses.*;

public class Team {
    private int x = 0; // Position X du joueur (départ en 0)
    private int y = 0; // Position Y du joueur (départ en 0)
    private List<Character> members = new ArrayList<>();

    public Team(){
        // On ajoute la Compagnie de Naheulbeuk
        members.add(new Ranger());
        members.add(new Dwarf());
        members.add(new Elf());
        members.add(new Barbarian());
        members.add(new Magician());
        members.add(new Ogre());
        members.add(new Thief());
    }

    // Méthodes pour modifier sa propre position
    public void moveNorth() { this.y--; }
    public void moveSouth() { this.y++; }
    public void moveEast()  { this.x++; }
    public void moveWest()  { this.x--; }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Character> getMembers() {
        return members;
    }

    public void setMembers(List<Character> members) {
        this.members = members;
    }
}
