package dev.kanazukii.banana.engine;

import org.joml.Vector2f;
import org.joml.Vector4f;

import dev.kanazukii.banana.engine.components.SpriteRenderer;
import dev.kanazukii.banana.engine.utils.AssetPool;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(){
        System.out.println("Level Editor Scene");
    }

    @Override
    public void init() {

        camera = new Camera(new Vector2f());

        GameObject go = new GameObject("Test Object", new Transform(new Vector2f(100, 100), new Vector2f(250, 250)));
        go.addComponent(new SpriteRenderer(new Vector4f(0,0, 0,1)));
        addGameObject(go);

        GameObject stranger = new GameObject("Stranger", new Transform(new Vector2f(350, 100), new Vector2f(250, 250)));
        stranger.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/textures/Stranger.png")));
        addGameObject(stranger);

        GameObject sorceress = new GameObject("Sorceress", new Transform(new Vector2f(600, 100), new Vector2f(250, 250)));
        sorceress.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/textures/Sorceress.png")));
        addGameObject(sorceress);

        System.out.println("Game Objects in Scene: " + gameObjects.size());

        loadAssets();
    }

    private void loadAssets(){
        AssetPool.getShader("assets/shaders/default.glsl");
    }

    @Override
    public void update(float deltaTime) {
        System.out.println("FPs: " + String.valueOf(Window.FPS));
        for (GameObject gameObject: gameObjects){
            gameObject.update(deltaTime);
        }

        renderer.render();
    }

    
}