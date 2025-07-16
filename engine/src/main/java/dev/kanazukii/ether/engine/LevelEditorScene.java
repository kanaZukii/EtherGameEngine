package dev.kanazukii.ether.engine;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.GsonBuildConfig;

import dev.kanazukii.ether.engine.components.RigidBody;
import dev.kanazukii.ether.engine.components.Sprite;
import dev.kanazukii.ether.engine.components.SpriteRenderer;
import dev.kanazukii.ether.engine.components.Spritesheet;
import dev.kanazukii.ether.engine.utils.AssetPool;
import imgui.ImGui;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(){
        System.out.println("Level Editor Scene");
    }

    private Spritesheet test_sheet;
    
    @Override
    public void init() {

        loadAssets();

        camera = new Camera(new Vector2f());

        test_sheet = AssetPool.getSpriteSheet("assets/textures/skeleton_spritesheet.png");
        
        if(sceneLoaded){
            this.activeGameObject = gameObjects.get(1);
            return;
        }

        GameObject testSquare = new GameObject("Square", new Transform(new Vector2f(100, 100), new Vector2f(250, 250)),0);
        SpriteRenderer testSpriteRenderer = new SpriteRenderer();
        testSpriteRenderer.setColor(new Vector4f(1,0,0,1));
        testSquare.addComponent(testSpriteRenderer);
        addGameObject(testSquare);

        SpriteRenderer skeletonSprite = new SpriteRenderer();
        skeletonSprite.setSprite(test_sheet.getSprite(0));
        GameObject skeleton = new GameObject("Skeleton", new Transform(new Vector2f(350, 100), new Vector2f(64, 64)), 1);
        skeleton.addComponent(skeletonSprite);
        skeleton.addComponent(new RigidBody());
        addGameObject(skeleton);


        this.activeGameObject = gameObjects.get(1);

    }
    
    // TODO: FIX Texture loading mismatch when load order changed (Texture frist time used in update)
    private void loadAssets(){
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/textures/skeleton_spritesheet.png", 
                                new Spritesheet(AssetPool.getTexture("assets/textures/skeleton_spritesheet.png"), 
                                    16, 16, 32, 0));
    }

    private float tick = 0.1f;
    private float current =  0.0f;
    private int spriteIndex = 4;

    @Override
    public void update(float deltaTime) {
        System.out.println("FPS: " + String.valueOf(Window.FPS));

        for (GameObject gameObject: gameObjects){
            gameObject.update(deltaTime);
        }

        current += deltaTime;
        if(current >= tick){
            current = 0;
            if(spriteIndex >= 8){
                spriteIndex = 4;
            }  
            gameObjects.get(1).getComponent(SpriteRenderer.class).setSprite(test_sheet.getSprite(spriteIndex));
            spriteIndex++;
        }

        if(gameObjects.get(1).transform != null){
             gameObjects.get(1).transform.position.x += 50 * deltaTime;
        }
        
        renderer.render();
    }

    @Override
    public void ImGUI(){
        ImGui.text("Hello World!");
        ImGui.button("Click me!");
        ImGui.end();
    }
    
}