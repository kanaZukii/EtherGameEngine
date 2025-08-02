package dev.kanazukii.ether.engine.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.components.SpriteRenderer;
import dev.kanazukii.ether.engine.components.Texture;

public class Renderer {
    
    // Max render batch size and list of all batches of the renderer
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;
    private static Shader currentShader; // Shader program to be used (glsl)

    public Renderer(){
        batches = new ArrayList<>();
    }

    // Add a game object that has a sprite renderer component, components that do not required to be draw should be ignored
    public void add(GameObject gameObject){
        SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);

        if(sprite != null){
            addToRender(sprite);
        }
    }

    // Add a sprite to the renderer, creates a new batch if a batch limit has been reached or the texture limit has been reached
    private void addToRender(SpriteRenderer sprite){
        boolean added = false;
        for(RenderBatch batch: batches){
            if(batch.hasRoom() && batch.zIndex() == sprite.gameObject.transform.zIndex){
                Texture tex = sprite.getTexture();
                if(tex == null  || batch.hasTexture(tex) || batch.hasRoomForTexture()){
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if(!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.transform.zIndex, this);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    // Loops through each batch and call the draw function
    public void render(){
        currentShader.use();
        for(int i = 0; i < batches.size(); i++){
            RenderBatch renderBatch = batches.get(i);
            renderBatch.render();
        }
    }

    public static Shader getBoundShader(){
        return currentShader;
    }


    public static void bindShader(Shader shader){
        currentShader = shader;
    }

    public void destroyGameObject(GameObject gameObject){
        if(gameObject.getComponent(SpriteRenderer.class) == null){
            return;
        }

        for(RenderBatch batch : batches){
            if(batch.destroyIfExist(gameObject)){
                return;
            }
        }
    }

}
