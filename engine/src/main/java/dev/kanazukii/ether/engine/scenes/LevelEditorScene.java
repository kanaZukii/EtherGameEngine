package dev.kanazukii.ether.engine.scenes;

import java.io.ObjectInputFilter.Config;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.GsonBuildConfig;

import dev.kanazukii.ether.engine.Camera;
import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.Prefabs;
import dev.kanazukii.ether.engine.components.GridLines;
import dev.kanazukii.ether.engine.components.MouseControls;
import dev.kanazukii.ether.engine.components.RigidBody;
import dev.kanazukii.ether.engine.components.Sprite;
import dev.kanazukii.ether.engine.components.SpriteRenderer;
import dev.kanazukii.ether.engine.components.Spritesheet;
import dev.kanazukii.ether.engine.components.Transform;
import dev.kanazukii.ether.engine.renderer.DebugRenderer;
import dev.kanazukii.ether.engine.utils.AssetPool;
import dev.kanazukii.ether.engine.utils.Configs;
import imgui.ImGui;
import imgui.ImVec2;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(){
        System.out.println("Level Editor Scene");
    }


    private GameObject Editor_Components = new GameObject("EDITOR");
    private Spritesheet test_sheet;
    private Spritesheet tile_set;

    @Override
    public void init() {

        Editor_Components.addComponent(new MouseControls());
        Editor_Components.addComponent(new GridLines());

        loadAssets();
        
        camera = new Camera(new Vector2f());
        tile_set = AssetPool.getSpriteSheet("assets/textures/tilemap.png");
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

        AssetPool.addSpriteSheet("assets/textures/tilemap.png", 
                                new Spritesheet(AssetPool.getTexture("assets/textures/tilemap.png"), 
                                    16, 16, 10, 0));

        AssetPool.addSpriteSheet("assets/textures/skeleton_spritesheet.png", 
                                new Spritesheet(AssetPool.getTexture("assets/textures/skeleton_spritesheet.png"), 
                                    16, 16, 8, 0));
    }

    private float tick = 0.1f;
    private float current =  0.0f;
    private int spriteIndex = 4;

    @Override
    public void update(float deltaTime) {
        //System.out.println("FPS: " + String.valueOf(Window.FPS));
        
        Editor_Components.update(deltaTime);

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
        
        renderer.render();
    }

    @Override
    public void ImGUI(){
        ImGui.begin("Tile Set");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for(int i = 0; i < tile_set.getSize(); i++){
            Sprite tile_icon = tile_set.getSprite(i);

            float icon_width = tile_icon.getWidth() * 4;
            float icon_height = tile_icon.getHeight() * 4;
            int id = tile_icon.getTexture().getID();
            Vector2f[] texCoords =  tile_icon.getTexCoords();

            ImGui.pushID(i);
            // Check if image button is clicked (I have fixed the texture by mirroring texcoords to the right h orientation)
            if(ImGui.imageButton(id, icon_width, icon_height, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                System.out.println("Clicked: " + i);
                GameObject tile = Prefabs.createSpriteObj(tile_icon, Configs.GRID_WIDTH, Configs.GRID_HEIGHT);
                Editor_Components.getComponent(MouseControls.class).pickUpObject(tile);
            }
            ImGui.popID();

            ImVec2 lasButtonPos = new ImVec2();
            ImGui.getItemRectMax(lasButtonPos);

            float lastButtonX2 = lasButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + icon_width;

            if(i + 1 < tile_set.getSize() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
    
}