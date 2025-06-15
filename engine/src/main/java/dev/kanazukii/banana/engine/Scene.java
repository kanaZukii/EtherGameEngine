package dev.kanazukii.banana.engine;

public abstract class Scene {
    
    protected Camera camera;

    public Scene(){
        
    }

    public abstract void update(float deltaTime);

    public void init(){
        
    }

}
