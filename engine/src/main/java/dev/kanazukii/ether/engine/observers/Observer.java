package dev.kanazukii.ether.engine.observers;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.observers.events.Event;

public interface Observer {
    
    void onNotify(GameObject gameObj, Event event);


}
