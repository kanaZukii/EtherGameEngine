package dev.kanazukii.ether.engine.components;

import java.util.ArrayList;
import java.util.List;

public class AnimationRenderer extends SpriteRenderer{

    private List<Sprite> sprites = new ArrayList<>();
    private float interval = 1.0f/12.0f;
    private transient float timer = 0.0f;
    private transient int currentIndex = 0;

    public AnimationRenderer(Spritesheet spriteSheet, int[] indices, float interval){
        this.interval = interval;
        for(int i = 0; i < indices.length; i++){
            sprites.add(spriteSheet.getSprite(indices[i]));
        }
        this.setSprite(sprites.get(0));
    }
    
    public AnimationRenderer(Spritesheet spriteSheet, int[] indices){
        for(int i = 0; i < indices.length; i++){
            sprites.add(spriteSheet.getSprite(indices[i]));
        }
        this.setSprite(sprites.get(0));
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);

        timer += deltaTime;
        if(timer >= interval){
            if(currentIndex >= sprites.size()) currentIndex = 0;
            this.setSprite(sprites.get(currentIndex));
            currentIndex++;
            timer = 0.0f;
        }
    }

    @Override
    public void editorUpdate(float deltaTime){
        super.update(deltaTime);

        timer += deltaTime;
        if(timer >= interval){
            if(currentIndex >= sprites.size()) currentIndex = 0;
            this.setSprite(sprites.get(currentIndex));
            currentIndex++;
            timer = 0.0f;
        }
    }
}
