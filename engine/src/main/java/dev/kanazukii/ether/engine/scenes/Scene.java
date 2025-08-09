package dev.kanazukii.ether.engine.scenes;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joml.Vector2f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.kanazukii.ether.engine.Camera;
import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.GameObjectDeserializer;
import dev.kanazukii.ether.engine.components.Component;
import dev.kanazukii.ether.engine.components.ComponentDeserializer;
import dev.kanazukii.ether.engine.components.Texture;
import dev.kanazukii.ether.engine.components.TextureDeserializer;
import dev.kanazukii.ether.engine.components.Transform;
import dev.kanazukii.ether.engine.physcis2D.Physics2D;
import dev.kanazukii.ether.engine.renderer.Renderer;
import dev.kanazukii.ether.engine.utils.AssetPool;

public class Scene {
    
    private Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean running = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    private Physics2D physics;

    private SceneInitializer sceneInit;

    public Scene(SceneInitializer sceneInit){
        this.sceneInit = sceneInit;
        this.physics = new Physics2D();
        this.renderer = new Renderer();
    }

    public boolean isRunning(){
        return running;
    }

    public void init(){
        camera = new Camera(new Vector2f());
        sceneInit.loadResource(this);
        sceneInit.init(this);
    }

    // Scene start method, add all game object to the renderer
    public void start(){
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.start();
            renderer.add(gameObject);
            physics.add(gameObject);
        }

        running = true;
    }

    public GameObject createGameObject(String name){
        GameObject gameObj = new GameObject(name);
        gameObj.addComponent(new Transform());
        gameObj.transform = gameObj.getComponent(Transform.class);
        return gameObj;
    }

    // Adds game objects to the scene and initialize them
    public void addGameObject(GameObject gameObject){
        if(!running){
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.start();
            renderer.add(gameObject);
            physics.add(gameObject);
        }
    }

    public List<GameObject> getGameObjects(){
        return gameObjects;
    }

    public GameObject getGameObjectByUID(int UID){
        Optional<GameObject> retrieve = this.gameObjects.stream()
                                .filter(gameObject -> gameObject.getUID() == UID)
                                .findFirst();
        return retrieve.orElse(null);
    }

    public Camera getCamera(){
        return camera;
    }

    public void ImGUI(){
        sceneInit.ImGUI();
    }

    public void save(){
        AssetPool.saveAssets();
        Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Component.class, new ComponentDeserializer())
                        .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                        .registerTypeAdapter(Texture.class, new TextureDeserializer())
                        .create();
        try {
            FileWriter writer = new FileWriter("gamescene.txt");
            List<GameObject> gameObjToSerialize = new ArrayList<>();
            for (GameObject gameObject : this.gameObjects) {
                if(gameObject.doSerialize()){
                    gameObjToSerialize.add(gameObject);
                }
            }
            writer.write(gson.toJson(gameObjToSerialize));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadScene(){
        AssetPool.loadAssets();
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
        }
    }

    public void editorUpdate(float deltaTime){
        camera.adjustProjection();

        for (int i = 0; i < gameObjects.size(); i++){
            GameObject gameObject = gameObjects.get(i);
            gameObject.editorUpdate(deltaTime);
            
            if(gameObject.isDead()){
                gameObjects.remove(i);
                renderer.destroyGameObject(gameObject);
                physics.destroyGameObject(gameObject);
                i--;
            }
        }

    }

    // Scene update logic
    public void update(float deltaTime){
        //System.out.println("FPS: " + String.valueOf(Window.FPS));
        camera.adjustProjection();
        physics.update(deltaTime);

        for (int i = 0; i < gameObjects.size(); i++){
            GameObject gameObject = gameObjects.get(i);
            gameObject.update(deltaTime);
            
            if(gameObject.isDead()){
                gameObjects.remove(i);
                renderer.destroyGameObject(gameObject);
                physics.destroyGameObject(gameObject);
                i--;
            }
        }
    };

     // Call your renderer to draw all the objects
    public void render(){
        this.renderer.render();
    };

    public void destroy(){
        for(GameObject gameObject : gameObjects){
            gameObject.destroy();
        }
    }

}
