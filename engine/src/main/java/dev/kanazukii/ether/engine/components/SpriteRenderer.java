package dev.kanazukii.ether.engine.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import dev.kanazukii.ether.engine.editor.EtherImGUi;

public class SpriteRenderer extends Component{
    
    // Color of the  object being drawn, defaults to white if has texture
    private Vector4f color = new Vector4f(1,1,1,1);
    // Sprite object that contains a sprite texture coordinates to sample from the texture (Spritesheet)
    private Sprite sprite = new Sprite();

    // Used for checking modification in scale, position and sprite index
    private transient Transform lastTransform;
    private transient boolean isDirty = false;

    public SpriteRenderer(){
        isDirty = true;
    }

    public void setDirty(){
        isDirty = true;
    }

    public boolean isDirty(){
        return isDirty;
    }

    public Vector4f getColor(){
        return color;
    }

    public void setTexture(Texture texture){
        sprite.setTexture(texture);
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
    public void editorUpdate(float deltaTime){
        if(!lastTransform.equals(gameObject.transform)){
            this.gameObject.transform.copyTo(lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void ImGUI(){
        if(EtherImGUi.colorPicker4f("Color Picker", color)){
            isDirty = true;
        };

    }
}
