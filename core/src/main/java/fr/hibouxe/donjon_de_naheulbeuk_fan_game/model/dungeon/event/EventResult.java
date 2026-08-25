package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event;

import java.util.List;
import java.util.ArrayList;

public class EventResult {
    private boolean blockMovement;
    private List<String> dialogsToDisplay;
    private String actionTrigger;

    public EventResult(boolean blockMovement, List<String> dialogsToDisplay) {
        this.blockMovement = blockMovement;
        this.dialogsToDisplay = dialogsToDisplay != null ? dialogsToDisplay : new ArrayList<>();
    }
    
    public EventResult(boolean blockMovement) {
        this(blockMovement, new ArrayList<>());
    }

    public boolean isBlockMovement() {
        return blockMovement;
    }

    public void setBlockMovement(boolean blockMovement) {
        this.blockMovement = blockMovement;
    }

    public List<String> getDialogsToDisplay() {
        return dialogsToDisplay;
    }

    public String getActionTrigger() {
        return actionTrigger;
    }

    public void setActionTrigger(String actionTrigger) {
        this.actionTrigger = actionTrigger;
    }

    public void setDialogsToDisplay(List<String> dialogsToDisplay) {
        this.dialogsToDisplay = dialogsToDisplay;
    }
}
