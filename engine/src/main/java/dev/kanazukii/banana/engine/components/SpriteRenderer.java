package dev.kanazukii.banana.engine.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import dev.kanazukii.banana.engine.Component;
import dev.kanazukii.banana.engine.Texture;
import dev.kanazukii.banana.engine.Transform;

public class SpriteRenderer extends Component{
    
    private Vector4f color;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty = false;

    public SpriteRenderer(Vector4f color){
        this.color = color;
        this.sprite = new Sprite(null);
    }

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

    @Override
    public void start(){
        lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float deltaTime){
        if(!lastTransform.equals(gameObject.transform)){
            this.gameObject.transform.copyTo(lastTransform);
            isDirty = true;
        }
    }
}
