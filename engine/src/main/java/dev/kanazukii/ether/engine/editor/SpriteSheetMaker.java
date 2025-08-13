package dev.kanazukii.ether.engine.editor;

import dev.kanazukii.ether.engine.components.Spritesheet;
import dev.kanazukii.ether.engine.components.Texture;
import dev.kanazukii.ether.engine.utils.AssetPool;
import imgui.ImColor;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImInt;

public class SpriteSheetMaker {
    
    private Texture texture;
    private int spriteWidth = 16, spriteHeight = 16, spacing = 16, numSprites = 16;
    private int viewScale = 4;
    private int maxSprite = 1;

    public SpriteSheetMaker(){

    }

    public void ImGUI(){
        ImGui.begin("Sprite Sheet Configuration");
        
        spritePreview();

        if(texture != null){
            maxSprite = (texture.getHeight()/spriteHeight) * (texture.getWidth()/spriteWidth);
        } 

        if (ImGui.beginTable("SpriteSettings", 2, ImGuiTableFlags.BordersInnerV)) {
            ImGui.tableNextRow();
            ImGui.tableSetColumnIndex(0);
            ImGui.text("Sprite Width:");
            ImGui.tableSetColumnIndex(1);
            ImGui.text("Sprite Height:");

            ImGui.tableNextRow();
            ImGui.tableSetColumnIndex(0);
            ImInt width = new ImInt(spriteWidth);
            ImGui.setNextItemWidth(120);
            if (ImGui.inputInt("##width", width)) {
                spriteWidth = width.get();
                if(spriteWidth <= 0) spriteWidth = 1;
            }
            ImGui.tableSetColumnIndex(1);
            ImInt height = new ImInt(spriteHeight);
            ImGui.setNextItemWidth(120);
            if (ImGui.inputInt("##height", height)) {
                spriteHeight = height.get();
                if(spriteHeight <= 0) spriteHeight = 1;
            }

            ImGui.tableNextRow();
            ImGui.tableSetColumnIndex(0);
            ImGui.text("Sprite Spacing:");
            ImGui.tableSetColumnIndex(1);
            ImGui.text("No. of Sprites:");

            ImGui.tableNextRow();
            ImGui.tableSetColumnIndex(0);
            ImInt spriteSpacing = new ImInt(spacing);
            ImGui.setNextItemWidth(120);
            if (ImGui.inputInt("##spacing", spriteSpacing)) {
                spacing = spriteSpacing.get();
                if(spacing < 0) spacing = 0;
            }
            ImGui.tableSetColumnIndex(1);
            ImInt totalSprites = new ImInt(numSprites);
            ImGui.setNextItemWidth(120);
            if (ImGui.inputInt("##numSprites", totalSprites)) {
                numSprites  = totalSprites.get();
                if(numSprites > maxSprite){
                    numSprites = maxSprite;
                }
                if(numSprites <= 0) numSprites = 1;
            }

            ImGui.endTable();
        }

        float windowWidth = ImGui.getWindowSizeX();
        float buttonWidth = 200.0f; 
        float buttonX = (windowWidth - buttonWidth) / 2.0f;

        ImGui.setCursorPosX(buttonX);
        if (ImGui.button("Create Sprite Sheet", buttonWidth, 0)) {
            Spritesheet newSheet = new Spritesheet(texture, spriteWidth, spriteHeight, numSprites, spacing);
            AssetPool.addSpriteSheet(texture.getFilePath(), newSheet);
            setTexture(null);
        }
        
        ImGui.end();
    }

    private void spritePreview(){
        float imgWidth  = texture.getWidth() * viewScale;
        float imgHeight = texture.getHeight() * viewScale;

        // Get position before drawing image
        float posX = ImGui.getCursorScreenPosX();
        float posY = ImGui.getCursorScreenPosY();

        ImGui.image(texture.getID(), imgWidth, imgHeight, 0, 1, 1, 0);

        // Now overlay a grid
        ImDrawList drawList = ImGui.getWindowDrawList();

        // Grid cell size (in pixels)
        float cellWidth = (spriteWidth + spacing) * viewScale;
        float cellHeight = (spriteHeight + spacing) * viewScale;

        // Vertical lines
        for (float x = posX; x <= posX + imgWidth; x += cellWidth) {
            drawList.addLine(x, posY, x, posY + imgHeight, ImColor.rgba(1f, 1f, 1f, 0.5f));
        }

        // Horizontal lines
        for (float y = posY; y <= posY + imgHeight; y += cellHeight) {
            drawList.addLine(posX, y, posX + imgWidth, y, ImColor.rgba(1f, 1f, 1f, 0.5f));
        }

        int totalCols = (int)(imgWidth / cellWidth);
        int totalRows = (int)(imgHeight / cellHeight);

        int drawn = 0;
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalCols; col++) {
                if (drawn >= numSprites) break;

                float x1 = posX + col * cellWidth;
                float y1 = posY + row * cellHeight;
                float x2 = x1 + cellWidth;
                float y2 = y1 + cellHeight;

                drawList.addRectFilled(
                    x1, y1,
                    x2, y2,
                    ImColor.rgba(1f, 1f, 0f, 0.3f)
                );

                drawn++;
            }
            if (drawn >= numSprites) break;
        }

    }

    public void setTexture(Texture texture){
        this.texture = texture;
        spriteWidth = 16;
        spriteHeight = 16;
        spacing = 0;
        numSprites = 1;
        maxSprite = 1;
    }

    public Texture getTexture(){
        return texture;
    }
}
