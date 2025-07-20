package dev.kanazukii.ether.engine;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.components.Sprite;
import dev.kanazukii.ether.engine.components.SpriteRenderer;
import dev.kanazukii.ether.engine.components.Transform;

public class Prefabs {
    
    public static GameObject createSpriteObj(Sprite sprite, float width, float height){
        GameObject spriteObj = new GameObject("SpriteObj_Generated",
                                                new Transform(new Vector2f(0,0), new Vector2f(width,height)),
                                                0);
        SpriteRenderer spriterenderer = new SpriteRenderer();
        spriterenderer.setSprite(sprite);
        spriteObj.addComponent(spriterenderer);

        return spriteObj;
    }

    public static GameObject duplicateSpriteObj(GameObject spriteObj){
        GameObject newSpriteObj = new GameObject("SpriteObj_GeneratedDup",
                                                spriteObj.transform.copy(),
                                                0);

        Sprite dupSprite = new Sprite();
        dupSprite.setTexture(spriteObj.getComponent(SpriteRenderer.class).getTexture());
        dupSprite.setTexCoords(spriteObj.getComponent(SpriteRenderer.class).getTexCoords());
        SpriteRenderer spriterenderer = new SpriteRenderer();
        spriterenderer.setSprite(dupSprite);
        newSpriteObj.addComponent(spriterenderer);

        return newSpriteObj;
    }

}
