package dev.kanazukii.ether.engine.observers;

import java.util.ArrayList;
import java.util.List;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.observers.events.Event;

public class EventSystem {
    

    private static List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer){
        observers.add(observer);
    }

    public static void notify(GameObject gameObj, Event event){
        for(Observer observer : observers){
            observer.onNotify(gameObj, event);
        }
    }
}
