package dev.kanazukii.banana.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    
    protected Camera camera;
    private boolean running = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){
        
    }

    public boolean isRunning(){
        return running;
    }

    public void init(){
        
    }

    public void start(){
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
        }

        running = true;
    }

    public void addGameObject(GameObject gameObject){
        if(!running){
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.start();
        }
    }

    public abstract void update(float deltaTime);

}
