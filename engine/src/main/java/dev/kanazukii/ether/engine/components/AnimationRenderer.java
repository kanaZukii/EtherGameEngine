package dev.kanazukii.ether.engine.components;

import java.util.List;

public class AnimationRenderer extends SpriteRenderer{

    private Spritesheet spriteSheet;
    private int[] indices;
    private float interval = 1.0f/12.0f;
    private transient float timer = 0.0f;
    private transient int currentIndex = 0;

    public AnimationRenderer(Spritesheet spriteSheet, int[] indices, float interval){
        this.spriteSheet = spriteSheet;
        this.indices = indices;
        this.interval = interval;
        this.setSprite(spriteSheet.getSprite(0));
    }
    
    public AnimationRenderer(Spritesheet spriteSheet, int[] indices){
        this.spriteSheet = spriteSheet;
        this.indices = indices;
        this.setSprite(spriteSheet.getSprite(0));
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);

        timer += deltaTime;
        if(timer >= interval){
            if(currentIndex >= indices.length) currentIndex = 0;
            this.setSprite(spriteSheet.getSprite(indices[currentIndex]));
            currentIndex++;
            timer = 0.0f;
        }
    }

    @Override
    public void editorUpdate(float deltaTime){
        super.update(deltaTime);

        timer += deltaTime;
        if(timer >= interval){
            if(currentIndex >= indices.length) currentIndex = 0;
            this.setSprite(spriteSheet.getSprite(indices[currentIndex]));
            currentIndex++;
            timer = 0.0f;
        }
    }
}
