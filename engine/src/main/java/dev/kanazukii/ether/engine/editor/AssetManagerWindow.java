package dev.kanazukii.ether.engine.editor;

import java.util.List;

import dev.kanazukii.ether.engine.components.Spritesheet;
import dev.kanazukii.ether.engine.components.Texture;
import dev.kanazukii.ether.engine.utils.AssetPool;
import dev.kanazukii.ether.engine.utils.FileManager;
import imgui.ImGui;

public class AssetManagerWindow {
    
    SpriteSheetMaker spriteSheetMake;

    public AssetManagerWindow(){
        spriteSheetMake = new SpriteSheetMaker();
    }

    public void ImGUI(){
        ImGui.begin("Asset Manager"+  "###AssetManager");

        List<Spritesheet> sheets = AssetPool.getLoadedSpriteSheets();

        for(Spritesheet sheet : sheets){

            Texture sheetTex = sheet.getTexture();
            if(ImGui.collapsingHeader(sheetTex.getFilePath())){

                float windowWidth = ImGui.getWindowSizeX();
                float nextX = (windowWidth - sheetTex.getWidth()) / 2.0f;
                ImGui.setCursorPosX(nextX);
                ImGui.image(sheetTex.getID(), sheetTex.getWidth(), sheetTex.getHeight(), 0,1,1,0);

                float buttonWidth = 200.0f; 
                nextX = (windowWidth - buttonWidth) / 2.0f;
                ImGui.setCursorPosX(nextX);
                if (ImGui.button("Delete Sprite Sheet", buttonWidth, 0)) {
                    AssetPool.removeSpriteSheet(sheetTex.getFilePath());
                }
            }
        }

        if(ImGui.button("Import Sprite Asset")){
            String path = FileManager.filePickerDialog("Image file (.png, .jpg)", "png", "jpg");
            System.out.println(path);
            if(path != null){
                spriteSheetMake.setTexture(AssetPool.getTexture(path));
            }
        }

        ImGui.end();
    }

    public void update(float deltaTime){
        if(spriteSheetMake.getTexture() != null){
            spriteSheetMake.ImGUI();
        }
    }
}
