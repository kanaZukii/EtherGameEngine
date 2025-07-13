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

    @Override
    public void init() {

        loadAssets();

        camera = new Camera(new Vector2f());

        Spritesheet test_sheet = AssetPool.getSpriteSheet("assets/textures/skeleton_spritesheet.png");

        GameObject testSquare = new GameObject("Square", new Transform(new Vector2f(100, 100), new Vector2f(250, 250)));
        testSquare.addComponent(new SpriteRenderer(new Vector4f(0,0, 0,1)));
        addGameObject(testSquare);

        skeleton = new GameObject("Skeleton", new Transform(new Vector2f(350, 100), new Vector2f(250, 250)));
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

    @Override
    public void update(float deltaTime) {
        System.out.println("FPs: " + String.valueOf(Window.FPS));
        for (GameObject gameObject: gameObjects){
            gameObject.update(deltaTime);
        }
        
        skeleton.transform.position.x += 50 * deltaTime;
        

        renderer.render();
    }

    
}