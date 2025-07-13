package dev.kanazukii.ether.engine;

import org.joml.Vector2f;
import org.joml.Vector4f;

import dev.kanazukii.ether.engine.components.Sprite;
import dev.kanazukii.ether.engine.components.SpriteRenderer;
import dev.kanazukii.ether.engine.components.Spritesheet;
import dev.kanazukii.ether.engine.utils.AssetPool;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(){
        System.out.println("Level Editor Scene");
    }

    private GameObject skeleton;
    private Spritesheet test_sheet;

    @Override
    public void init() {

        loadAssets();

        camera = new Camera(new Vector2f());

        test_sheet = AssetPool.getSpriteSheet("assets/textures/skeleton_spritesheet.png");

        GameObject testSquare = new GameObject("Square", new Transform(new Vector2f(100, 100), new Vector2f(250, 250)),0);
        testSquare.addComponent(new SpriteRenderer(new Vector4f(0,0, 0,1)));
        addGameObject(testSquare);

        skeleton = new GameObject("Skeleton", new Transform(new Vector2f(350, 100), new Vector2f(64, 64)), 1);
        skeleton.addComponent(new SpriteRenderer(test_sheet.getSprite(0)));
        addGameObject(skeleton);

        System.out.println("Game Objects in Scene: " + gameObjects.size());
    }

    private void loadAssets(){
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/textures/skeleton_spritesheet.png", 
                                new Spritesheet(AssetPool.getTexture("assets/textures/skeleton_spritesheet.png"), 
                                    16, 16, 32, 0));
    }

    private float tick = 0;
    private int spriteIndex = 4;
    

    @Override
    public void update(float deltaTime) {
        System.out.println("FPS: " + String.valueOf(Window.FPS));
        for (GameObject gameObject: gameObjects){
            gameObject.update(deltaTime);
        }

        if(tick >= 0.1f){
            tick = 0;
            spriteIndex++;
            if(spriteIndex >= 8){
                spriteIndex = 4;
            }
            skeleton.getComponent(SpriteRenderer.class).setSprite(test_sheet.getSprite(spriteIndex));
        }
        
        skeleton.transform.position.x += 50 * deltaTime;
        
        tick += deltaTime;
        renderer.render();
    }

    
}