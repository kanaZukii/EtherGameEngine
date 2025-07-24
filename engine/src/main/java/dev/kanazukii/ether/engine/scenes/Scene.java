package dev.kanazukii.ether.engine.scenes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.kanazukii.ether.engine.Camera;
import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.GameObjectDeserializer;
import dev.kanazukii.ether.engine.components.Component;
import dev.kanazukii.ether.engine.components.ComponentDeserializer;
import dev.kanazukii.ether.engine.components.Texture;
import dev.kanazukii.ether.engine.components.TextureDeserializer;
import dev.kanazukii.ether.engine.renderer.Renderer;
import imgui.ImGui;

public abstract class Scene {
    
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean running = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean sceneLoaded = false;

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
            ImGui.begin("Inspector: " + activeGameObject.getName());
            activeGameObject.ImGUI();
            ImGui.end();
        }
    }

    public void ImGUI(){

    }

    public void saveAtExit(){
        Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Component.class, new ComponentDeserializer())
                        .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                        .registerTypeAdapter(Texture.class, new TextureDeserializer())
                        .create();
        try {
            FileWriter writer = new FileWriter("gamescene.txt");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadScene(){
        Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Component.class, new ComponentDeserializer())
                        .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                        .registerTypeAdapter(Texture.class, new TextureDeserializer())
                        .create();
        String savedScene = "";

        try{
            savedScene = new String(Files.readAllBytes(Paths.get("gamescene.txt")));
        } catch (IOException e){
            e.printStackTrace();
        }

        if(!savedScene.equals("")){
            int maxGameObjectID = -1;
            int maxComponentID = -1;
            GameObject[] gameObjects = gson.fromJson(savedScene, GameObject[].class);
            for(GameObject go: gameObjects){
                addGameObject(go);

                for(Component component : go.getComponentList()){
                    if(component.getUID() > maxComponentID){
                        maxComponentID = component.getUID();
                    }
                }

                if(go.getUID() > maxGameObjectID){
                        maxGameObjectID = go.getUID();
                    }
            }

            maxGameObjectID++;
            maxComponentID++;
            System.out.println(maxGameObjectID);
            System.out.println(maxComponentID);
            GameObject.init(maxGameObjectID);
            Component.init(maxComponentID);
            sceneLoaded = true;
        }
    }

    // Scene update logic
    public abstract void update(float deltaTime);

     // Call your renderer to draw all the objects
    public abstract void render();

}
