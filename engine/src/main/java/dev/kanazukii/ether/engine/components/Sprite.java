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

    // // A constructor for creating a sprite from an entire texture (Whole Image) (Before Gson)
    // public Sprite(Texture texture){
    //     this.texture = texture;
    //     Vector2f[] texCoords = {
    //         new Vector2f(1,1),
    //         new Vector2f(1,0),
    //         new Vector2f(0,0),
    //         new Vector2f(0,1)
    //     };

    //     this.texCoords = texCoords;
    // }

    // // Constructor for a singular sprite within a provided sprite sheet texture to sample from
    // public Sprite(Texture texture, Vector2f[] texCoords){
    //     this.texture = texture;
    //     this.texCoords = texCoords;
    // }

    public Sprite(){

    }

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] texCoords){
        this.texCoords = texCoords;
    }

    public Texture getTexture(){
        return texture;
    }

    public Vector2f[] getTexCoords(){
        return texCoords;
    }

}
