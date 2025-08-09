package dev.kanazukii.ether.engine.components;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

public class Spritesheet {
    
    private Texture texture;
    private int spriteWidth, spriteHeight, numSprites, spacing;
    private transient List<Sprite> sprites = new ArrayList<>();

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing){
        this.texture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.numSprites = numSprites;
        this.spacing = spacing;

        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight; // Bottom corner of the first top left sprite in the sheet

        // Retreiving Texture coordinates  Normalizing based on its pixel height and pixel width and sprite height and width
        for(int i = 0; i < numSprites; i++){
            float topY = (float) (currentY + spriteHeight)/(float) texture.getHeight();
            float rightX = (float) (currentX + spriteWidth)/(float) texture.getWidth();
            float bottomY = (float)currentY/ (float)texture.getHeight();
            float leftX = (float)currentX/ (float)texture.getWidth();

            Vector2f[] texCoords = {
                new Vector2f(rightX,topY),
                new Vector2f(rightX,bottomY),
                new Vector2f(leftX,bottomY),
                new Vector2f(leftX,topY)
            };

            Sprite newSprite = new Sprite();
            newSprite.setTexture(texture);
            newSprite.setTexCoords(texCoords);
            newSprite.setWidth(spriteWidth);
            newSprite.setHeight(spriteHeight);
            
            sprites.add(newSprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth()){
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index){
        return sprites.get(index);
    }

    public Texture getTexture(){
        return texture;
    }

    public int getSize(){
        return sprites.size();
    }
}
