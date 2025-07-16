package dev.kanazukii.ether.engine.components;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.Texture;

public class Sprite {
    
    private Texture texture = null;
    private Vector2f[] texCoords = {
            new Vector2f(1,1),
            new Vector2f(1,0),
            new Vector2f(0,0),
            new Vector2f(0,1)
        };
    
    private float spriteWidth;
    private float spriteHeight;

    public Sprite(){

    }

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] texCoords){
        this.texCoords = texCoords;
    }

    public void setWidth(float width){
        this.spriteWidth = width;
    }

    public void setHeight(float height){
        this.spriteHeight = height;
    }

    public Texture getTexture(){
        return texture;
    }

    public Vector2f[] getTexCoords(){
        return texCoords;
    }

    public float getWidth(){
        return spriteWidth;
    }

    public float getHeight(){
        return spriteHeight;
    }

}
