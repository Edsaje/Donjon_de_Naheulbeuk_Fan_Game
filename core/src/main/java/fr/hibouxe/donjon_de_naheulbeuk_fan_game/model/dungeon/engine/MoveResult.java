package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.engine;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EventResult;

public class MoveResult {
    public enum MoveStatus { SUCCESS, BLOCKED, EVENT_TRIGGERED }
    
    private MoveStatus status;
    private EventResult eventResult;

    public MoveResult(MoveStatus status, EventResult eventResult) {
        this.status = status;
        this.eventResult = eventResult;
    }
    
    public MoveResult(MoveStatus status) {
        this(status, null);
    }

    public MoveStatus getStatus() { return status; }
    public EventResult getEventResult() { return eventResult; }
}