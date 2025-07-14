package dev.kanazukii.ether.engine;

import java.util.ArrayList;
import java.util.List;

import dev.kanazukii.ether.engine.renderer.Renderer;
import imgui.ImGui;

public abstract class Scene {
    
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean running = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;

    public Scene(){
        
    }

    public boolean isRunning(){
        return running;
    }

    public void init(){
        
    }

    // Scene start method, add all game object to the renderer
    public void start(){
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            renderer.add(gameObject);
        }

        running = true;
    }

    // Adds game objects to the scene and initialize them
    public void addGameObject(GameObject gameObject){
        if(!running){
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.start();
            renderer.add(gameObject);
        }
    }

    public Camera getCamera(){
        return camera;
    }

    public void sceneImGUI(){
        if(activeGameObject != null){
            ImGui.begin("Object Inspector");
            activeGameObject.ImGUI();
            ImGui.end();
        }
    }

    public void ImGUI(){

    }

    // Scene update logic
    public abstract void update(float deltaTime);

}
