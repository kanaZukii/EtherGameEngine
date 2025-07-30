package dev.kanazukii.ether.engine.scenes;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.Camera;
import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.Prefabs;
import dev.kanazukii.ether.engine.components.EditorCamera;
import dev.kanazukii.ether.engine.components.GizmoTools;
import dev.kanazukii.ether.engine.components.GridLines;
import dev.kanazukii.ether.engine.components.MouseControls;
import dev.kanazukii.ether.engine.components.Sprite;
import dev.kanazukii.ether.engine.components.Spritesheet;
import dev.kanazukii.ether.engine.utils.AssetPool;
import dev.kanazukii.ether.engine.utils.Configs;
import imgui.ImGui;
import imgui.ImVec2;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(){
        System.out.println("Level Editor Scene");
    }


    private GameObject Editor_Components = new GameObject("EDITOR");
    private Spritesheet tile_set;

    @Override
    public void init() {

        camera = new Camera(new Vector2f());

        loadAssets();

        Editor_Components.addComponent(new EditorCamera(camera));
        Editor_Components.addComponent(new MouseControls());
        Editor_Components.addComponent(new GridLines());
        Editor_Components.addComponent(new GizmoTools(AssetPool.getSpriteSheet("assets/editor/gizmo_sprites.png")));

        Editor_Components.start();

        tile_set = AssetPool.getSpriteSheet("assets/textures/tilemap.png");
        
        if(sceneLoaded){
            return;
        }

    }
    
    // TODO: FIX Texture loading mismatch when load order changed (Texture frist time used in update)
    private void loadAssets(){
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/editor/gizmo_sprites.png",
                                new Spritesheet(AssetPool.getTexture("assets/editor/gizmo_sprites.png"),
                                128, 256, 2, 0));

        AssetPool.addSpriteSheet("assets/textures/tilemap.png", 
                                new Spritesheet(AssetPool.getTexture("assets/textures/tilemap.png"), 
                                    16, 16, 10, 0));

        AssetPool.addSpriteSheet("assets/textures/skeleton_spritesheet.png", 
                                new Spritesheet(AssetPool.getTexture("assets/textures/skeleton_spritesheet.png"), 
                                    16, 16, 8, 0));
    }

    @Override
    public void update(float deltaTime) {
        //System.out.println("FPS: " + String.valueOf(Window.FPS));
        camera.adjustProjection();
        Editor_Components.update(deltaTime);

        for (GameObject gameObject: gameObjects){
            gameObject.update(deltaTime);
        }

    }

    private void editorImGUI(){
        ImGui.begin("Editor Components");
        Editor_Components.ImGUI();
        ImGui.end();

    }

    @Override
    public void ImGUI(){
        editorImGUI();

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

            float icon_width = tile_icon.getWidth() * 3;
            float icon_height = tile_icon.getHeight() * 3;
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

    @Override
    public void render() {
        renderer.render();
    }
    
}