package dev.kanazukii.banana.engine.renderer;

import java.util.ArrayList;
import java.util.List;

import dev.kanazukii.banana.engine.GameObject;
import dev.kanazukii.banana.engine.Texture;
import dev.kanazukii.banana.engine.components.SpriteRenderer;

public class Renderer {
    
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer(){
        batches = new ArrayList<>();
    }

    public void add(GameObject gameObject){
        SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);

        if(sprite != null){
            addToRender(sprite);
        }
    }

    private void addToRender(SpriteRenderer sprite){
        boolean added = false;
        for(RenderBatch batch: batches){
            if(batch.hasRoom()){
                Texture tex = sprite.getTexture();
                if(tex == null  || batch.hasTexture(tex) || batch.hasRoomForTexture()){
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if(!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
        }
    }

    public void render(){
        for(RenderBatch batch: batches){
            batch.render();
        }
    }

}
