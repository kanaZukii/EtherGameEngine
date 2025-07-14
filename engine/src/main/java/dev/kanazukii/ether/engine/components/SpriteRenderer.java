package dev.kanazukii.ether.engine.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import dev.kanazukii.ether.engine.Component;
import dev.kanazukii.ether.engine.Texture;
import dev.kanazukii.ether.engine.Transform;
import imgui.ImGui;

public class SpriteRenderer extends Component{
    
    // Color of the  object being drawn, defaults to white if has texture
    private Vector4f color = new Vector4f(1,1,1,1);
    // Sprite object that contains a sprite texture coordinates to sample from the texture (Spritesheet)
    private Sprite sprite = new Sprite();

    // Used for checking modification in scale, position and sprite index
    private transient Transform lastTransform;
    private transient boolean isDirty = false;

    // // Construtor for a sprite renderer for a colored shape (Before gson)
    // public SpriteRenderer(Vector4f color){
    //     this.color = color;
    //     this.sprite = new Sprite(null);
    //     isDirty = true;
    // }

    // // Construtor for a sprite renderer to render a sampled sprite from a spritesheet
    // public SpriteRenderer(Sprite sprite){
    //     this.sprite = sprite;
    //     this.color = new Vector4f(1,1,1,1);
    //     isDirty = true;
    // }

    public SpriteRenderer(){
        isDirty = true;
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
        isDirty = true;
        this.sprite =  sprite;
    }

    public void setColor(Vector4f color){
        if(!this.color.equals(color)){
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

    @Override
    public void ImGUI(){
        float[] colors = {this.color.x, this.color.y, this.color.z, this.color.w}; 
        if(ImGui.colorPicker4("Color Picker", colors)){
            this.color.set(colors[0], colors[1], colors[2], colors[3]);
            isDirty = true;
        };

    }
}
