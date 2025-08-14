package dev.kanazukii.ether.engine.scenes;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

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
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;

public class LevelEditorInitializer extends SceneInitializer {

    public LevelEditorInitializer(){
        System.out.println("Level Editor Scene");
    }


    private GameObject Editor_Components;
    private Spritesheet tile_set;

    @Override
    public void init(Scene scene) {
        Editor_Components = scene.createGameObject("EDITOR");
        Editor_Components.setSerialize(false);

        Editor_Components.addComponent(new EditorCamera(scene.camera));
        Editor_Components.addComponent(new MouseControls());
        Editor_Components.addComponent(new GridLines());
        Editor_Components.addComponent(new GizmoTools(AssetPool.getSpriteSheet("assets/editor/gizmo_sprites.png")));

        scene.addGameObject(Editor_Components);
        
    }
    
    // TODO: FIX Texture loading mismatch when load order changed (Texture frist time used in update)
    @Override
    public void loadResource(Scene scene){
        AssetPool.getShader("assets/shaders/default.glsl");
    }

    private void editorImGUI(){
        ImGui.begin("Editor Components");
        Editor_Components.ImGUI();
        ImGui.end();

    }

    @Override
    public void ImGUI(){
        editorImGUI();

        ImGui.begin("Tile Set", ImGuiWindowFlags.MenuBar);

        if (ImGui.beginMenuBar()) {
            List<String> paths = new ArrayList<>();
            for(Spritesheet sheet : AssetPool.getLoadedSpriteSheets()){
                paths.add(sheet.getTexture().getFilePath());
            }
            String[] tileSets = paths.toArray(new String[0]);

            // Position for right aligned combo
            float menuBarWidth = ImGui.getWindowSizeX();
            float comboWidth = 300.0f; 
            float cursorPosX = menuBarWidth - comboWidth - ImGui.getStyle().getFramePaddingX() * 2;

            ImGui.setCursorPosX(cursorPosX);


            ImGui.setNextItemWidth(comboWidth);
            ImInt selected = new ImInt(0);
            if (ImGui.beginCombo("##tileSetSelect", "Select Tile Set")) {
                for (int i = 0; i < tileSets.length; i++) {
                    if (ImGui.selectable(tileSets[i])) {
                        selected.set(i);
                        tile_set = AssetPool.getSpriteSheet(tileSets[i]);
                    }
                }
                ImGui.endCombo();
            }

            ImGui.endMenuBar();
        }

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        if(tile_set != null){
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
        }

        ImGui.end();
    }
}