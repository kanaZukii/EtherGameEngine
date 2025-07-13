package dev.kanazukii.ether.engine.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import dev.kanazukii.ether.engine.Component;
import dev.kanazukii.ether.engine.Texture;
import dev.kanazukii.ether.engine.Transform;

public class SpriteRenderer extends Component{
    
    // Color of the  object being drawn, defaults to white if has texture
    private Vector4f color;
    // Sprite object that contains a sprite texture coordinates to sample from the texture (Spritesheet)
    private Sprite sprite;

    // Used for checking modification in scale, position and sprite index
    private Transform lastTransform;
    private boolean isDirty = false;

    // Construtor for a sprite renderer for a colored shape
    public SpriteRenderer(Vector4f color){
        this.color = color;
        this.sprite = new Sprite(null);
    }

    // Construtor for a sprite renderer to render a sampled sprite from a spritesheet
    public SpriteRenderer(Sprite sprite){
        this.sprite = sprite;
        this.color = new Vector4f(1,1,1,1);
    }

    public boolean isDirty(){
        return isDirty;
    }

    public Vector4f getColor(){
        return color;
    }

    public Texture getTexture(){
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords(){
        return sprite.getTexCoords();
    }

    public void setClean(){
        isDirty = false;
    }

    public void setSprite(Sprite sprite){
        this.sprite =  sprite;
        isDirty = true;
    }

    public void setColor(Vector4f color){
        if(!color.equals(color)){
            isDirty = true;
            this.color.set(color);
        }
    }

    // Get the intital copy of the transform
    @Override
    public void start(){
        lastTransform = gameObject.transform.copy();
    }

    // Check every update if the transform is modified
    @Override
    public void update(float deltaTime){
        if(!lastTransform.equals(gameObject.transform)){
            this.gameObject.transform.copyTo(lastTransform);
            isDirty = true;
        }
    }
}
