package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.Controller;

public class InputManager {
    private InputListener listener;
    private long lastMoveTime = 0;
    private long lastActionTime = 0;

    public void setListener(InputListener listener) {
        this.listener = listener;
    }

    public void update() {
        if (listener == null) return;
        
        Controller pad = null;
        if (Controllers.getControllers().size > 0) {
            pad = Controllers.getControllers().first();
        }

        boolean actionTriggered = false;
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) { listener.onInput("B"); actionTriggered = true; }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { listener.onInput("ENTER"); actionTriggered = true; }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.X) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { listener.onInput("X"); actionTriggered = true; }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) { listener.onInput("C"); actionTriggered = true; }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) { listener.onInput("I"); actionTriggered = true; }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.K)) { listener.onInput("K"); actionTriggered = true; }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) { listener.onInput("1"); actionTriggered = true; }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) { listener.onInput("2"); actionTriggered = true; }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)) { listener.onInput("3"); actionTriggered = true; }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4)) { listener.onInput("4"); actionTriggered = true; }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5)) { listener.onInput("5"); actionTriggered = true; }

        if (!actionTriggered && pad != null && System.currentTimeMillis() - lastActionTime > 250) {
            if (pad.getButton(pad.getMapping().buttonA)) { listener.onInput("ENTER"); actionTriggered = true; }
            else if (pad.getButton(pad.getMapping().buttonB)) { listener.onInput("X"); actionTriggered = true; }
            else if (pad.getButton(pad.getMapping().buttonStart)) { listener.onInput("X"); actionTriggered = true; }
            else if (pad.getButton(pad.getMapping().buttonX)) { listener.onInput("C"); actionTriggered = true; }
            else if (pad.getButton(pad.getMapping().buttonY)) { listener.onInput("K"); actionTriggered = true; }
            
            if (actionTriggered) lastActionTime = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() - lastMoveTime > 250) {
            boolean padUp = false, padDown = false, padLeft = false, padRight = false;
            if (pad != null) {
                padUp = pad.getButton(pad.getMapping().buttonDpadUp) || pad.getAxis(pad.getMapping().axisLeftY) < -0.5f;
                padDown = pad.getButton(pad.getMapping().buttonDpadDown) || pad.getAxis(pad.getMapping().axisLeftY) > 0.5f;
                padLeft = pad.getButton(pad.getMapping().buttonDpadLeft) || pad.getAxis(pad.getMapping().axisLeftX) < -0.5f;
                padRight = pad.getButton(pad.getMapping().buttonDpadRight) || pad.getAxis(pad.getMapping().axisLeftX) > 0.5f;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.Z) || Gdx.input.isKeyPressed(Input.Keys.UP) || padUp) {
                listener.onInput("Z");
                lastMoveTime = System.currentTimeMillis();
            } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || padDown) {
                listener.onInput("S");
                lastMoveTime = System.currentTimeMillis();
            } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || padLeft) {
                listener.onInput("Q");
                lastMoveTime = System.currentTimeMillis();
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || padRight) {
                listener.onInput("D");
                lastMoveTime = System.currentTimeMillis();
            }
        }
    }
}
