package dev.kanazukii.ether.engine.observers.events;

public class Event {
    public EventType type;

    public Event(EventType type){
        this.type = type;
    }

    public Event(){
        type = EventType.GameEvent;
    }
}
