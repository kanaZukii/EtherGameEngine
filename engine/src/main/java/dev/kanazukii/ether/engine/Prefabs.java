package dev.kanazukii.ether.engine;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.components.Sprite;
import dev.kanazukii.ether.engine.components.SpriteRenderer;
import dev.kanazukii.ether.engine.components.Transform;

public class Prefabs {
    
    public static GameObject createSpriteObj(Sprite sprite, float width, float height){
        return createSpriteObj(sprite, width, height, 0);
    }

    public static GameObject createSpriteObj(Sprite sprite, float width, float height, int zIndex){
        GameObject spriteObj = Window.getScene().createGameObject("SpriteObj_Generated");
        spriteObj.transform.scale.x = width;
        spriteObj.transform.scale.y = height;
        spriteObj.transform.zIndex = zIndex;
        SpriteRenderer spriterenderer = new SpriteRenderer();
        spriterenderer.setSprite(sprite);
        spriteObj.addComponent(spriterenderer);

        return spriteObj;
    }

    public static GameObject duplicateSpriteObj(GameObject spriteObj){
        GameObject spriteDup = Window.getScene().createGameObject("SpriteObj_Generated");
    
        Sprite dupSprite = new Sprite();
        dupSprite.setTexture(spriteObj.getComponent(SpriteRenderer.class).getTexture());
        dupSprite.setTexCoords(spriteObj.getComponent(SpriteRenderer.class).getTexCoords());
        SpriteRenderer spriterenderer = new SpriteRenderer();
        spriterenderer.setSprite(dupSprite);
        spriteDup.addComponent(spriterenderer);
        spriteDup.transform.copyTo(spriteDup.transform);

        return spriteDup;
    }

}
